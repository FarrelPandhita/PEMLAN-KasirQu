package com.kasirqu.facade;

import com.kasirqu.contracts.TransactionContract;
import com.kasirqu.models.CartItem;
import com.kasirqu.models.CheckoutResult;
import com.kasirqu.models.Transaksi;
import com.kasirqu.services.create.CreateTransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade that orchestrates transaction and checkout operations.
 * The GUI interacts ONLY with this class for checkout and transaction history.
 *
 * Architecture: GUI → TransactionFacade → CreateTransactionService → Repository → DB
 *
 * Status:
 *   CHECKOUT  → ✅ Fully wired (CreateTransactionService)
 *   HISTORY   → ⏳ Stub (waiting for ReadTransactionService)
 */
public class TransactionFacade implements TransactionContract {

    private final CreateTransactionService createService;

    public TransactionFacade() {
        this.createService = new CreateTransactionService();
    }

    // ═══════════════════════════════════════════════════════════
    // CHECKOUT — Delegated to CreateTransactionService ✅
    // ═══════════════════════════════════════════════════════════

    /**
     * Full checkout with all financial parameters.
     *
     * @param cartItems    snapshot of cart items
     * @param namaOperator the cashier name
     * @param diskon       discount amount
     * @param pajak        tax amount
     * @param metodeBayar  payment method (cash/transfer/qris)
     * @param catatan      optional notes
     * @return CheckoutResult with invoice number and summary
     * @throws SQLException if checkout fails (all changes rolled back)
     */
    @Override
    public CheckoutResult checkout(List<CartItem> cartItems, String namaOperator,
                                   BigDecimal diskon, BigDecimal pajak,
                                   String metodeBayar, String catatan) throws SQLException {
        return createService.checkout(cartItems, namaOperator, diskon, pajak, metodeBayar, catatan);
    }

    /**
     * Simple checkout (cash, no discount/tax).
     */
    @Override
    public CheckoutResult checkout(List<CartItem> cartItems, String namaOperator) throws SQLException {
        return createService.checkout(cartItems, namaOperator);
    }

    // ═══════════════════════════════════════════════════════════
    // HISTORY — Stub ⏳ (wire when ReadTransactionService is ready)
    // ═══════════════════════════════════════════════════════════

    @Override
    public List<Transaksi> getTransactionHistory(int limit, int offset) {
        // TODO: Wire to ReadTransactionService when implemented
        return new ArrayList<>();
    }
}
