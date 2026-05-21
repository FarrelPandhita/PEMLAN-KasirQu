package com.kasirqu.contracts;

import com.kasirqu.models.DetailTransaksi;
import java.util.List;

public interface CartContract {
    List<DetailTransaksi> getCartItems();
    boolean addItem(DetailTransaksi item);
    boolean updateItemQty(int idBarang, int qty);
    boolean clearCart();
}
