package com.kasirqu.repositories.create;

import com.kasirqu.database.DatabaseConnection;
import com.kasirqu.models.Barang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository for CREATE operations on inventory (barang) and stock (stok_log).
 * Handles direct database interactions for inserting products and stock logs.
 *
 * Ownership: CREATE Developer
 * Do NOT add READ/UPDATE/DELETE queries here.
 */
public class CreateInventoryRepository {

    /**
     * Inserts a new product into the barang table.
     *
     * @param barang the product to insert (must have kodeBarang, namaBarang, harga, stok, idKategori)
     * @return the auto-generated id_barang, or -1 if insert failed
     * @throws SQLException if a database error occurs
     */
    public int insertBarang(Barang barang) throws SQLException {
        String sql = "INSERT INTO barang (id_kategori, kode_barang, nama_barang, harga, stok, minimal_stok, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, 'aktif')";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, barang.getIdKategori());
            ps.setString(2, barang.getKodeBarang());
            ps.setString(3, barang.getNamaBarang());
            ps.setBigDecimal(4, barang.getHarga());
            ps.setInt(5, barang.getStok());
            ps.setInt(6, barang.getMinimalStok() > 0 ? barang.getMinimalStok() : 5);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Checks if a product with the given kode_barang already exists.
     *
     * @param kodeBarang the product code to check
     * @return true if a product with this code exists (including soft-deleted)
     * @throws SQLException if a database error occurs
     */
    public boolean existsByKodeBarang(String kodeBarang) throws SQLException {
        String sql = "SELECT COUNT(*) FROM barang WHERE kode_barang = ?";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Increases stock for a given product.
     *
     * @param idBarang the product ID
     * @param qty      the quantity to add (must be positive)
     * @return true if the update affected at least one row
     * @throws SQLException if a database error occurs
     */
    public boolean increaseStock(int idBarang, int qty) throws SQLException {
        String sql = "UPDATE barang SET stok = stok + ? WHERE id_barang = ? AND deleted_at IS NULL";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, idBarang);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Inserts a stock log entry for auditing purposes.
     *
     * @param idBarang     the product ID
     * @param jenis        the log type: 'masuk', 'keluar', or 'adjustment'
     * @param qty          the quantity involved
     * @param stokSebelum  stock before the operation
     * @param stokSesudah  stock after the operation
     * @param keterangan   description of the operation
     * @throws SQLException if a database error occurs
     */
    public void insertStokLog(int idBarang, String jenis, int qty,
                              int stokSebelum, int stokSesudah,
                              String keterangan) throws SQLException {
        String sql = "INSERT INTO stok_log (id_barang, jenis, qty, stok_sebelum, stok_sesudah, keterangan) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
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
     * Gets the current stock for a product by id.
     * Used internally for stok_log calculation (stok_sebelum).
     *
     * @param idBarang the product ID
     * @return current stock, or -1 if product not found
     * @throws SQLException if a database error occurs
     */
    public int getCurrentStock(int idBarang) throws SQLException {
        String sql = "SELECT stok FROM barang WHERE id_barang = ? AND deleted_at IS NULL";

        Connection conn = DatabaseConnection.getConnection();
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
