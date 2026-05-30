package com.kasirqu.services.read;

import com.kasirqu.models.Barang;
import com.kasirqu.repositories.read.ReadInventoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for READ operations on inventory.
 * Delegates to ReadInventoryRepository and provides business-level filtering.
 *
 * Architecture: GUI → Facade → ReadInventoryService → ReadInventoryRepository → DB
 *
 * Ownership: READ Developer
 */
public class ReadInventoryService {

    private final ReadInventoryRepository repository;

    public ReadInventoryService() {
        this.repository = new ReadInventoryRepository();
    }

    public ReadInventoryService(ReadInventoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetches paginated products from the database.
     *
     * @param limit  max items per page
     * @param offset starting index
     * @return list of Barang
     */
    public List<Barang> getProducts(int limit, int offset) {
        return repository.selectBarang(limit, offset);
    }

    /**
     * Searches products by keyword matching nama_barang or kode_barang.
     * Uses client-side filtering over a broad fetch since ReadInventoryRepository
     * currently only supports selectBarang(limit, offset).
     *
     * @param keyword the search term
     * @return filtered list of Barang
     */
    public List<Barang> searchProduct(String keyword) {
        // Fetch a large set and filter client-side
        // TODO: READ developer should add a proper SQL LIKE query in repository
        List<Barang> all = repository.selectBarang(1000, 0);
        if (keyword == null || keyword.trim().isEmpty()) {
            return all;
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        return all.stream()
                .filter(b -> b.getNamaBarang().toLowerCase().contains(lowerKeyword)
                          || b.getKodeBarang().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Gets the total count of products.
     * TODO: READ developer should add a COUNT(*) query in repository.
     *
     * @return total product count
     */
    public int getTotalProductCount() {
        return repository.selectBarang(Integer.MAX_VALUE, 0).size();
    }
}
