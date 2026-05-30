package com.kasirqu.facade;

import com.kasirqu.contracts.InventoryContract;
import com.kasirqu.models.Barang;
import com.kasirqu.services.create.CreateInventoryService;
import com.kasirqu.services.read.ReadInventoryService;
import com.kasirqu.services.update.UpdateInventoryService;
import com.kasirqu.services.delete.DeleteInventoryService;

import java.sql.SQLException;
import java.util.List;

/**
 * Facade that orchestrates all CRUD inventory services.
 * The GUI interacts ONLY with this class for inventory operations.
 *
 * Architecture: GUI → InventoryFacade → [Create|Read|Update|Delete]InventoryService → Repository → DB
 *
 * Status:
 *   CREATE → ✅ Fully wired (CreateInventoryService)
 *   READ   → ✅ Fully wired (ReadInventoryService)
 *   UPDATE → ⏳ Stub (waiting for feature/update integration)
 *   DELETE → ✅ Fully wired (DeleteInventoryService)
 */
public class InventoryFacade implements InventoryContract {

    private final CreateInventoryService createService;
    private final ReadInventoryService   readService;
    private final UpdateInventoryService updateService;
    private final DeleteInventoryService deleteService;

    public InventoryFacade() {
        this.createService = new CreateInventoryService();
        this.readService   = new ReadInventoryService();
        this.updateService = new UpdateInventoryService();
        this.deleteService = new DeleteInventoryService();
    }

    // ═══════════════════════════════════════════════════════════
    // CREATE — Delegated to CreateInventoryService ✅
    // ═══════════════════════════════════════════════════════════

    @Override
    public int createProduct(Barang barang) throws SQLException {
        return createService.createProduct(barang);
    }

    @Override
    public void increaseStock(int idBarang, int qty) throws SQLException {
        createService.increaseStock(idBarang, qty);
    }

    // ═══════════════════════════════════════════════════════════
    // READ — Delegated to ReadInventoryService ✅
    // ═══════════════════════════════════════════════════════════

    @Override
    public Barang getProductByCode(String code) {
        // Use search and find first match
        List<Barang> results = readService.searchProduct(code);
        for (Barang b : results) {
            if (b.getKodeBarang().equalsIgnoreCase(code)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Barang> getProducts(int limit, int offset) {
        return readService.getProducts(limit, offset);
    }

    @Override
    public List<Barang> searchProduct(String keyword) {
        return readService.searchProduct(keyword);
    }

    @Override
    public int getTotalProductCount() {
        return readService.getTotalProductCount();
    }

    // ═══════════════════════════════════════════════════════════
    // UPDATE — Stub ⏳ (wire when feature/update is ready)
    // ═══════════════════════════════════════════════════════════

    @Override
    public boolean updateProduct(Barang barang) throws SQLException {
        // TODO: Wire to updateService.updateProduct(barang) when implemented
        return updateService.updateProduct(barang);
    }

    // ═══════════════════════════════════════════════════════════
    // DELETE — Stub ⏳ (wire when feature/delete is ready)
    // ═══════════════════════════════════════════════════════════

    @Override
    public boolean deleteProduct(int idBarang) throws SQLException {
        return deleteService.deleteProduct(idBarang);
    }
}
