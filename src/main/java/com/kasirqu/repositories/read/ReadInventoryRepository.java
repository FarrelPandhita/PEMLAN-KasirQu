package com.kasirqu.repositories.read;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kasirqu.database.DatabaseConnection;
import com.kasirqu.models.Barang;

public class ReadInventoryRepository {

    public List<Barang> selectBarang(int limit, int offset) {

        List<Barang> listBarang = new ArrayList<>();

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            String sql =
                    "SELECT * FROM barang LIMIT ? OFFSET ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Barang barang = new Barang();

                barang.setIdBarang(
                        rs.getInt("id_barang")
                );

                barang.setIdKategori(
                        rs.getInt("id_kategori")
                );

                barang.setKodeBarang(
                        rs.getString("kode_barang")
                );

                barang.setNamaBarang(
                        rs.getString("nama_barang")
                );

                barang.setHarga(
                        rs.getBigDecimal("harga")
                );

                barang.setStok(
                        rs.getInt("stok")
                );

                barang.setMinimalStok(
                        rs.getInt("minimal_stok")
                );

                barang.setStatus(
                        rs.getString("status")
                );

                barang.setCreatedAt(
                        rs.getTimestamp("created_at")
                );

                barang.setUpdatedAt(
                        rs.getTimestamp("updated_at")
                );

                barang.setDeletedAt(
                        rs.getTimestamp("deleted_at")
                );

                listBarang.add(barang);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listBarang;
    }
}