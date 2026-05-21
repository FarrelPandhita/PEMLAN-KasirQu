package com.kasirqu.facade;

import com.kasirqu.models.DetailTransaksi;
import com.kasirqu.contracts.CartContract;
import java.util.List;

public class CartFacade implements CartContract {
    
    // TODO: Inject CRUD Services here
    
    @Override
    public List<DetailTransaksi> getCartItems() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean addItem(DetailTransaksi item) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean updateItemQty(int idBarang, int qty) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean clearCart() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
