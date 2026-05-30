package com.kasirqu.repositories.delete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kasirqu.database.DatabaseConnection;

public class SoftDelete {

    public boolean softDeleteKategori(int idKategori) {
        String sql = "UPDATE kategori SET deleted_at = CURRENT_TIMESTAMP WHERE id_kategori = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idKategori);

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Gagal soft delete kategori: " + e.getMessage());
            return false;
        }
    }
}