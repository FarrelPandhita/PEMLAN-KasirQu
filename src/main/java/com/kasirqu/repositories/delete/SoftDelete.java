package com.kasirqu.repositories.delete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kasirqu.database.DatabaseConnection;

public class SoftDelete {

    public boolean softDeleteKategori(int idKategori) {
        String sql = "UPDATE kategori SET deleted_at = CURRENT_TIMESTAMP WHERE id_kategori = ?";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idKategori);

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Gagal soft delete kategori: " + e.getMessage());
            return false;
        }
    }

    public boolean softDeleteBarang(int idBarang) {
        String sql = "UPDATE barang SET deleted_at = CURRENT_TIMESTAMP, status = 'nonaktif' WHERE id_barang = ?";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBarang);

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Gagal soft delete barang: " + e.getMessage());
            return false;
        }
    }
}