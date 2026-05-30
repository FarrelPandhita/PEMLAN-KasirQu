package com.kasirqu;

import com.kasirqu.facade.InventoryFacade;
import com.kasirqu.models.Barang;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class TestIntegration {
    public static void main(String[] args) {
        System.out.println("=== STARTING INTEGRATION TEST ===");
        InventoryFacade inventoryFacade = new InventoryFacade();

        try {
            // 1. Create Data (Testing Flag: testing-blablabla)
            System.out.println("\n[1] Testing CREATE...");
            Barang newBarang = new Barang();
            newBarang.setKodeBarang("TEST-" + System.currentTimeMillis());
            newBarang.setNamaBarang("testing-blablabla Product");
            newBarang.setHarga(new BigDecimal("15000"));
            newBarang.setStok(10);
            newBarang.setIdKategori(1); // Assuming 1 exists (e.g. Makanan/Pakaian)

            int idBarang = inventoryFacade.createProduct(newBarang);
            System.out.println("[SUCCESS] Product created with ID: " + idBarang + ", Kode: " + newBarang.getKodeBarang());

            // 2. Read Data
            System.out.println("\n[2] Testing READ...");
            List<Barang> allProducts = inventoryFacade.getProducts(100, 0);
            boolean found = false;
            for (Barang b : allProducts) {
                if (b.getIdBarang() == idBarang) {
                    found = true;
                    System.out.println("[SUCCESS] Found created product in database: " + b.getNamaBarang() + " (Stok: " + b.getStok() + ")");
                }
            }
            if (!found) {
                System.err.println("[FAILED] Failed to find product after creation.");
            }

            // 3. Update Stock (via Create module)
            System.out.println("\n[3] Testing INCREASE STOCK...");
            inventoryFacade.increaseStock(idBarang, 5);
            Barang updatedBarang = inventoryFacade.getProductByCode(newBarang.getKodeBarang());
            if (updatedBarang != null && updatedBarang.getStok() == 15) {
                System.out.println("[SUCCESS] Stock updated successfully to: " + updatedBarang.getStok());
            } else {
                System.err.println("[FAILED] Failed to update stock.");
            }

            // 4. Delete Data
            System.out.println("\n[4] Testing DELETE...");
            boolean deleted = inventoryFacade.deleteProduct(idBarang);
            if (deleted) {
                System.out.println("[SUCCESS] Product soft-deleted successfully.");
            } else {
                System.err.println("[FAILED] Failed to delete product.");
            }

            // 5. Read Data Again (should not find it because of deleted_at IS NULL filter)
            System.out.println("\n[5] Testing READ AFTER DELETE...");
            Barang deletedBarang = inventoryFacade.getProductByCode(newBarang.getKodeBarang());
            if (deletedBarang == null) {
                System.out.println("[SUCCESS] Product no longer appears in READ results (Filtered successfully).");
            } else {
                System.err.println("[FAILED] Product still appears in READ results!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== INTEGRATION TEST FINISHED ===");
        System.exit(0);
    }
}
