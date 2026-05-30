package com.kasirqu.services.create;

import com.kasirqu.exceptions.DuplicateProductException;
import com.kasirqu.exceptions.ValidationException;
import com.kasirqu.models.Barang;
import com.kasirqu.repositories.create.CreateInventoryRepository;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Service for CREATE operations on inventory.
 * Handles product creation and stock addition with full validation.
 *
 * Architecture: GUI → Facade → CreateInventoryService → CreateInventoryRepository → DB
 *
 * Ownership: CREATE Developer
 */
public class CreateInventoryService {

    private final CreateInventoryRepository repository;

    public CreateInventoryService() {
        this.repository = new CreateInventoryRepository();
    }

    /**
     * Constructor for dependency injection (testability).
     */
    public CreateInventoryService(CreateInventoryRepository repository) {
        this.repository = repository;
    }

    // ═══════════════════════════════════════════════════════════
    // CREATE PRODUCT
    // ═══════════════════════════════════════════════════════════

    /**
     * Creates a new product in the database after full validation.
     *
     * Validations:
     * - nama_barang must not be empty
     * - kode_barang must not be empty
     * - harga must be > 0
     * - stok must be >= 0
     * - id_kategori must be > 0
     * - kode_barang must be unique
     *
     * @param barang the product data to insert
     * @return the auto-generated id_barang
     * @throws ValidationException if validation fails
     * @throws DuplicateProductException if kode_barang already exists
     * @throws SQLException if a database error occurs
     */
    public int createProduct(Barang barang) throws SQLException {
        // ── Input validation ─────────────────────────────────
        validateProductInput(barang);

        // ── Duplicate check ──────────────────────────────────
        if (repository.existsByKodeBarang(barang.getKodeBarang())) {
            throw new DuplicateProductException(barang.getKodeBarang());
        }

        // ── Insert ───────────────────────────────────────────
        int generatedId = repository.insertBarang(barang);
        if (generatedId == -1) {
            throw new SQLException("Gagal menyimpan produk ke database.");
        }

        // ── Insert initial stock log if stock > 0 ────────────
        if (barang.getStok() > 0) {
            repository.insertStokLog(
                generatedId,
                "masuk",
                barang.getStok(),
                0,                     // stok_sebelum (brand new product)
                barang.getStok(),      // stok_sesudah
                "Stok awal produk baru"
            );
        }

        return generatedId;
    }

    // ═══════════════════════════════════════════════════════════
    // ADD STOCK (INCREASE)
    // ═══════════════════════════════════════════════════════════

    /**
     * Increases stock for an existing product and logs the addition.
     *
     * @param idBarang the product ID to restock
     * @param qty      the quantity to add (must be > 0)
     * @throws ValidationException if qty is invalid
     * @throws SQLException if a database error occurs
     */
    public void increaseStock(int idBarang, int qty) throws SQLException {
        // ── Validation ───────────────────────────────────────
        if (idBarang <= 0) {
            throw new ValidationException("ID barang tidak valid.");
        }
        if (qty <= 0) {
            throw new ValidationException("Jumlah stok yang ditambahkan harus lebih dari 0.");
        }

        // ── Get current stock for log ────────────────────────
        int currentStock = repository.getCurrentStock(idBarang);
        if (currentStock == -1) {
            throw new ValidationException("Barang dengan ID " + idBarang + " tidak ditemukan.");
        }

        // ── Update stock ─────────────────────────────────────
        boolean success = repository.increaseStock(idBarang, qty);
        if (!success) {
            throw new SQLException("Gagal menambah stok untuk barang ID: " + idBarang);
        }

        // ── Insert stock log ─────────────────────────────────
        repository.insertStokLog(
            idBarang,
            "masuk",
            qty,
            currentStock,
            currentStock + qty,
            "Restok manual (+)" + qty
        );
    }

    // ═══════════════════════════════════════════════════════════
    // PRIVATE VALIDATION
    // ═══════════════════════════════════════════════════════════

    /**
     * Validates all required fields of a Barang before insertion.
     */
    private void validateProductInput(Barang barang) {
        if (barang == null) {
            throw new ValidationException("Data produk tidak boleh kosong.");
        }
        if (barang.getNamaBarang() == null || barang.getNamaBarang().trim().isEmpty()) {
            throw new ValidationException("Nama produk tidak boleh kosong.");
        }
        if (barang.getKodeBarang() == null || barang.getKodeBarang().trim().isEmpty()) {
            throw new ValidationException("Kode produk tidak boleh kosong.");
        }
        if (barang.getIdKategori() <= 0) {
            throw new ValidationException("Kategori produk harus dipilih.");
        }
        if (barang.getHarga() == null || barang.getHarga().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Harga produk harus lebih dari 0.");
        }
        if (barang.getStok() < 0) {
            throw new ValidationException("Stok awal tidak boleh negatif.");
        }
    }
}
