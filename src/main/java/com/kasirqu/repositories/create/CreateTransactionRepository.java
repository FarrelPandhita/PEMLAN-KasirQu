package com.kasirqu.repositories.create;

import com.kasirqu.database.DatabaseConnection;
import com.kasirqu.models.CartItem;
import com.kasirqu.models.DetailTransaksi;
import com.kasirqu.models.Transaksi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Repository for CREATE operations on transactions.
 * Handles inserting transaksi, detail_transaksi, stock reduction,
 * stok_log entries, and invoice number generation.
 *
 * All checkout mutations use SQL TRANSACTION for atomicity.
 *
 * Ownership: CREATE Developer
 * Do NOT add READ/UPDATE/DELETE queries here.
 */
public class CreateTransactionRepository {

    /**
     * Generates the next invoice number in format TRX-YYYYMMDD-XXXX.
     * Queries the latest transaction code for today's date to avoid collision.
     *
     * @param conn the active connection (part of transaction)
     * @return the next invoice number, e.g. TRX-20260530-0001
     * @throws SQLException if a database error occurs
     */
    public String generateInvoiceNumber(Connection conn) throws SQLException {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "TRX-" + today + "-";

        String sql = "SELECT kode_transaksi FROM transaksi "
                   + "WHERE kode_transaksi LIKE ? "
                   + "ORDER BY kode_transaksi DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastCode = rs.getString("kode_transaksi");
                    // Extract the sequence number (last 4 digits)
                    String sequencePart = lastCode.substring(lastCode.lastIndexOf('-') + 1);
                    int nextSequence = Integer.parseInt(sequencePart) + 1;
                    return prefix + String.format("%04d", nextSequence);
                }
            }
        }

        // First transaction of the day
        return prefix + "0001";
    }

    /**
     * Inserts a transaction record into the transaksi table.
     *
     * @param conn      the active connection (part of transaction)
     * @param transaksi the transaction data to insert
     * @return the auto-generated id_transaksi
     * @throws SQLException if a database error occurs
     */
    public int insertTransaksi(Connection conn, Transaksi transaksi) throws SQLException {
        String sql = "INSERT INTO transaksi "
                   + "(kode_transaksi, nama_operator, subtotal, diskon, pajak, grand_total, metode_bayar, catatan) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, transaksi.getKodeTransaksi());
            ps.setString(2, transaksi.getNamaOperator());
            ps.setBigDecimal(3, transaksi.getSubtotal());
            ps.setBigDecimal(4, transaksi.getDiskon() != null ? transaksi.getDiskon() : BigDecimal.ZERO);
            ps.setBigDecimal(5, transaksi.getPajak() != null ? transaksi.getPajak() : BigDecimal.ZERO);
            ps.setBigDecimal(6, transaksi.getGrandTotal());
            ps.setString(7, transaksi.getMetodeBayar() != null ? transaksi.getMetodeBayar() : "cash");
            ps.setString(8, transaksi.getCatatan());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Gagal menyimpan transaksi, tidak ada baris yang terpengaruh.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Gagal mendapatkan ID transaksi.");
            }
        }
    }

    /**
     * Inserts a single detail_transaksi row for a cart item.
     *
     * @param conn          the active connection (part of transaction)
     * @param idTransaksi   the parent transaction ID
     * @param item          the cart item to persist
     * @throws SQLException if a database error occurs
     */
    public void insertDetailTransaksi(Connection conn, int idTransaksi, CartItem item) throws SQLException {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_barang, qty, harga_satuan, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            ps.setInt(2, item.getBarang().getIdBarang());
            ps.setInt(3, item.getQty());
            ps.setBigDecimal(4, item.getBarang().getHarga());
            ps.setBigDecimal(5, item.getSubtotal());
            ps.executeUpdate();
        }
    }

    /**
     * Reduces stock for a product after checkout.
     *
     * @param conn      the active connection (part of transaction)
     * @param idBarang  the product ID
     * @param qty       the quantity to deduct
     * @throws SQLException if a database error occurs
     */
    public void reduceStock(Connection conn, int idBarang, int qty) throws SQLException {
        String sql = "UPDATE barang SET stok = stok - ? WHERE id_barang = ? AND deleted_at IS NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, idBarang);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Gagal mengurangi stok untuk barang ID: " + idBarang);
            }
        }
    }

    /**
     * Inserts a stok_log entry during checkout (type: keluar).
     *
     * @param conn         the active connection (part of transaction)
     * @param idBarang     the product ID
     * @param qty          the quantity sold
     * @param stokSebelum  stock before deduction
     * @param stokSesudah  stock after deduction
     * @param keterangan   description
     * @throws SQLException if a database error occurs
     */
    public void insertStokLog(Connection conn, int idBarang, String jenis, int qty,
                              int stokSebelum, int stokSesudah,
                              String keterangan) throws SQLException {
        String sql = "INSERT INTO stok_log (id_barang, jenis, qty, stok_sebelum, stok_sesudah, keterangan) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBarang);
            ps.setString(2, jenis);
            ps.setInt(3, qty);
            ps.setInt(4, stokSebelum);
            ps.setInt(5, stokSesudah);
            ps.setString(6, keterangan);
            ps.executeUpdate();
        }
    }

    /**
     * Reads the current stock of a product within a transaction.
     * Uses FOR UPDATE to lock the row during checkout.
     *
     * @param conn     the active connection (part of transaction)
     * @param idBarang the product ID
     * @return current stock, or -1 if not found
     * @throws SQLException if a database error occurs
     */
    public int getCurrentStockForUpdate(Connection conn, int idBarang) throws SQLException {
        String sql = "SELECT stok FROM barang WHERE id_barang = ? AND deleted_at IS NULL FOR UPDATE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stok");
                }
            }
        }
        return -1;
    }
}
