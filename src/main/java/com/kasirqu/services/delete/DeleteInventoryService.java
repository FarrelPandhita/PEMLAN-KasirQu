package com.kasirqu.services.delete;

import com.kasirqu.repositories.delete.SoftDelete;

public class DeleteInventoryService {
    
    private final SoftDelete repository;

    public DeleteInventoryService() {
        this.repository = new SoftDelete();
    }

    public boolean deleteProduct(int idBarang) {
        return repository.softDeleteBarang(idBarang);
    }
}
