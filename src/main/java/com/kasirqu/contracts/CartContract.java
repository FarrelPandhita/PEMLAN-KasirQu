package com.kasirqu.contracts;

import com.kasirqu.models.CartItem;
import com.kasirqu.models.Barang;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade contract for in-memory cart operations.
 * The GUI calls ONLY these methods via CartFacade.
 *
 * Cart is purely in-memory — no database table.
 */
public interface CartContract {

    void addItem(Barang barang, int qty);
    boolean updateItemQty(int idBarang, int newQty);
    boolean increaseQty(int idBarang, int delta);
    boolean removeItem(int idBarang);
    void clearCart();

    List<CartItem> getCartItems();
    int getItemCount();
    boolean isEmpty();

    BigDecimal calculateSubtotal();
    int calculateTotalQty();
}
