package com.kasirqu.services.update;

import com.kasirqu.models.Barang;
import com.kasirqu.repositories.update.UpdateInventoryRepository;

public class UpdateInventoryService {

    private final UpdateInventoryRepository repository =
            new UpdateInventoryRepository();

    public boolean updateBarang(Barang barang) {

        if (barang == null) {
            return false;
        }

        if (barang.getNamaBarang() == null ||
                barang.getNamaBarang().isBlank()) {

            return false;
        }

        if (barang.getHarga() == null) {
            return false;
        }

        if (barang.getStok() < 0) {
            return false;
        }

        return repository.updateBarang(barang);
    }
}