package com.kasirqu.services.create;

import com.kasirqu.database.DatabaseConnection;
import com.kasirqu.exceptions.EmptyCartException;
import com.kasirqu.exceptions.InsufficientStockException;
import com.kasirqu.exceptions.ValidationException;
import com.kasirqu.models.CartItem;
import com.kasirqu.models.CheckoutResult;
import com.kasirqu.models.Transaksi;
import com.kasirqu.repositories.create.CreateTransactionRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for CREATE operations on transactions.
 * Implements the full transactional checkout flow with commit/rollback.
 *
 * CHECKOUT FLOW:
 *   1. Validate cart not empty
 *   2. Validate operator name
 *   3. Begin SQL TRANSACTION
 *   4. Validate stock for each item (SELECT ... FOR UPDATE)
 *   5. Generate invoice number (TRX-YYYYMMDD-XXXX)
 *   6. Insert transaksi record
 *   7. Insert detail_transaksi for each cart item
 *   8. Reduce stock for each item
 *   9. Insert stok_log (type: keluar) for each item
 *  10. COMMIT
 *  11. Return CheckoutResult with invoice
 *
 * If ANY step fails: ROLLBACK all changes. NO PARTIAL SAVE.
 *
 * Architecture: GUI → Facade → CreateTransactionService → CreateTransactionRepository → DB
 *
 * Ownership: CREATE Developer
 */
public class CreateTransactionService {

    private final CreateTransactionRepository repository;

    public CreateTransactionService() {
        this.repository = new CreateTransactionRepository();
    }

    /**
     * Constructor for dependency injection (testability).
     */
    public CreateTransactionService(CreateTransactionRepository repository) {
        this.repository = repository;
    }

    // ═══════════════════════════════════════════════════════════
    // CHECKOUT (MAIN ENTRY POINT)
    // ═══════════════════════════════════════════════════════════

    /**
     * Performs the complete checkout flow as a single database transaction.
     *
     * @param cartItems      the list of items from the cart (snapshot)
     * @param namaOperator   the cashier/operator name for this session
     * @param diskon         discount amount (can be zero)
     * @param pajak          tax amount (can be zero)
     * @param metodeBayar    payment method: "cash", "transfer", or "qris"
     * @param catatan        optional notes
     * @return CheckoutResult containing invoice number and transaction summary
     * @throws EmptyCartException if cart is empty
     * @throws ValidationException if operator name is empty or payment method invalid
     * @throws InsufficientStockException if stock is insufficient for any item
     * @throws SQLException if a database error occurs (all changes rolled back)
     */
    public CheckoutResult checkout(List<CartItem> cartItems, String namaOperator,
                                   BigDecimal diskon, BigDecimal pajak,
                                   String metodeBayar, String catatan) throws SQLException {

        // ── Pre-transaction validation ───────────────────────
        validateCheckoutInput(cartItems, namaOperator, metodeBayar);

        // ── Calculate financial totals ───────────────────────
        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal discountAmount = diskon != null ? diskon : BigDecimal.ZERO;
        BigDecimal taxAmount = pajak != null ? pajak : BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.subtract(discountAmount).add(taxAmount);

        if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
            grandTotal = BigDecimal.ZERO;
        }

        // ── Begin SQL TRANSACTION ────────────────────────────
        Connection conn = DatabaseConnection.getConnection();
        boolean originalAutoCommit = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            // STEP 1: Validate stock with row-level locking
            validateStockAvailability(conn, cartItems);

            // STEP 2: Generate invoice number
            String kodeTransaksi = repository.generateInvoiceNumber(conn);

            // STEP 3: Build and insert transaksi record
            Transaksi transaksi = new Transaksi();
            transaksi.setKodeTransaksi(kodeTransaksi);
            transaksi.setNamaOperator(namaOperator);
            transaksi.setSubtotal(subtotal);
            transaksi.setDiskon(discountAmount);
            transaksi.setPajak(taxAmount);
            transaksi.setGrandTotal(grandTotal);
            transaksi.setMetodeBayar(metodeBayar != null ? metodeBayar : "cash");
            transaksi.setCatatan(catatan);

            int idTransaksi = repository.insertTransaksi(conn, transaksi);

            // STEP 4: Insert detail_transaksi + reduce stock + log for each item
            for (CartItem item : cartItems) {
                int idBarang = item.getBarang().getIdBarang();
                int qty = item.getQty();

                // 4a. Insert detail
                repository.insertDetailTransaksi(conn, idTransaksi, item);

                // 4b. Get current stock (already locked by validateStockAvailability)
                int currentStock = repository.getCurrentStockForUpdate(conn, idBarang);

                // 4c. Reduce stock
                repository.reduceStock(conn, idBarang, qty);

                // 4d. Insert stok_log (type: keluar)
                repository.insertStokLog(
                    conn,
                    idBarang,
                    "keluar",
                    qty,
                    currentStock,
                    currentStock - qty,
                    "Penjualan " + kodeTransaksi
                );
            }

            // ── COMMIT ───────────────────────────────────────
            conn.commit();

            // ── Build result ─────────────────────────────────
            return new CheckoutResult(
                idTransaksi,
                kodeTransaksi,
                namaOperator,
                Timestamp.valueOf(LocalDateTime.now()),
                subtotal,
                discountAmount,
                taxAmount,
                grandTotal,
                metodeBayar != null ? metodeBayar : "cash",
                catatan,
                cartItems
            );

        } catch (InsufficientStockException e) {
            // ── ROLLBACK on stock error ──────────────────────
            safeRollback(conn);
            throw e;

        } catch (SQLException e) {
            // ── ROLLBACK on any database error ───────────────
            safeRollback(conn);
            throw new SQLException("Checkout gagal. Semua perubahan dibatalkan. Detail: " + e.getMessage(), e);

        } catch (Exception e) {
            // ── ROLLBACK on unexpected error ─────────────────
            safeRollback(conn);
            throw new SQLException("Checkout gagal karena error tidak terduga: " + e.getMessage(), e);

        } finally {
            // ── Restore auto-commit ──────────────────────────
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException ignored) {
                // Best effort restore
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SIMPLE CHECKOUT (convenience overload)
    // ═══════════════════════════════════════════════════════════

    /**
     * Simplified checkout with no discount, no tax, cash payment.
     * Convenience method for the most common checkout scenario.
     */
    public CheckoutResult checkout(List<CartItem> cartItems, String namaOperator) throws SQLException {
        return checkout(cartItems, namaOperator, BigDecimal.ZERO, BigDecimal.ZERO, "cash", null);
    }

    // ═══════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════

    /**
     * Validates checkout input before starting the transaction.
     */
    private void validateCheckoutInput(List<CartItem> cartItems, String namaOperator, String metodeBayar) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException();
        }
        if (namaOperator == null || namaOperator.trim().isEmpty()) {
            throw new ValidationException("Nama operator tidak boleh kosong.");
        }

        // Validate each cart item
        for (CartItem item : cartItems) {
            if (item.getBarang() == null) {
                throw new ValidationException("Cart mengandung item dengan data produk kosong.");
            }
            if (item.getQty() <= 0) {
                throw new ValidationException("Jumlah pembelian untuk '" +
                    item.getBarang().getNamaBarang() + "' harus lebih dari 0.");
            }
        }

        // Validate payment method if provided
        if (metodeBayar != null && !metodeBayar.isEmpty()) {
            if (!metodeBayar.equals("cash") && !metodeBayar.equals("transfer") && !metodeBayar.equals("qris")) {
                throw new ValidationException("Metode pembayaran tidak valid: " + metodeBayar +
                    ". Gunakan: cash, transfer, atau qris.");
            }
        }
    }

    /**
     * Validates that sufficient stock exists for all cart items.
     * Uses SELECT ... FOR UPDATE to lock rows during the transaction.
     */
    private void validateStockAvailability(Connection conn, List<CartItem> cartItems) throws SQLException {
        for (CartItem item : cartItems) {
            int idBarang = item.getBarang().getIdBarang();
            int requestedQty = item.getQty();

            int currentStock = repository.getCurrentStockForUpdate(conn, idBarang);
            if (currentStock == -1) {
                throw new ValidationException("Produk '" + item.getBarang().getNamaBarang() +
                    "' tidak ditemukan di database.");
            }
            if (currentStock < requestedQty) {
                throw new InsufficientStockException(
                    idBarang,
                    item.getBarang().getNamaBarang(),
                    requestedQty,
                    currentStock
                );
            }
        }
    }

    /**
     * Calculates total subtotal from cart items.
     */
    private BigDecimal calculateSubtotal(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * Safely rolls back a connection, swallowing any secondary exceptions.
     */
    private void safeRollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException rollbackEx) {
            System.err.println("CRITICAL: Rollback failed - " + rollbackEx.getMessage());
            rollbackEx.printStackTrace();
        }
    }
}
