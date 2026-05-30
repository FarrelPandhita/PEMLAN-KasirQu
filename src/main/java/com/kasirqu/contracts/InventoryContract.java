package com.kasirqu.contracts;

import com.kasirqu.models.Barang;
import java.sql.SQLException;
import java.util.List;

/**
 * Facade contract for inventory operations.
 * The GUI calls ONLY these methods via InventoryFacade.
 */
public interface InventoryContract {

    // ── CREATE ───────────────────────────────────────────────
    int createProduct(Barang barang) throws SQLException;
    void increaseStock(int idBarang, int qty) throws SQLException;

    // ── READ ─────────────────────────────────────────────────
    Barang getProductByCode(String code);
    List<Barang> getProducts(int limit, int offset);
    List<Barang> searchProduct(String keyword);
    int getTotalProductCount();

    // ── UPDATE (stub — will be wired when feature/update merges) ──
    boolean updateProduct(Barang barang) throws SQLException;

    // ── DELETE (stub — will be wired when feature/delete merges) ──
    boolean deleteProduct(int idBarang) throws SQLException;
}
