package com.kasirqu.facade;

import com.kasirqu.models.Barang;
import com.kasirqu.contracts.InventoryContract;
import java.util.List;

public class InventoryFacade implements InventoryContract {
    
    // TODO: Inject CRUD Services here
    
    @Override
    public boolean createProduct(Barang barang) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Barang getProductByCode(String code) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Barang> getProducts(int limit, int offset) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Barang> searchProduct(String keyword) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean updateProduct(Barang barang) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean deleteProduct(int idBarang) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
