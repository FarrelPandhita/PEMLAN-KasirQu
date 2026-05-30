package com.kasirqu.repositories.update;

import com.kasirqu.database.DatabaseConnection;
import com.kasirqu.models.Barang;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateInventoryRepository {

    public boolean updateBarang(Barang barang) {

        String sql = """
            UPDATE barang
            SET
                id_kategori = ?,
                kode_barang = ?,
                nama_barang = ?,
                harga = ?,
                stok = ?,
                minimal_stok = ?,
                status = ?
            WHERE id_barang = ?
        """;

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            PreparedStatement stmt =
                    conn.prepareStatement(sql);

            stmt.setInt(1, barang.getIdKategori());

            stmt.setString(2,
                    barang.getKodeBarang());

            stmt.setString(3,
                    barang.getNamaBarang());

            stmt.setBigDecimal(4,
                    barang.getHarga());

            stmt.setInt(5,
                    barang.getStok());

            stmt.setInt(6,
                    barang.getMinimalStok());

            stmt.setString(7,
                    barang.getStatus());

            stmt.setInt(8,
                    barang.getIdBarang());

            int result = stmt.executeUpdate();

            stmt.close();

            return result > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}