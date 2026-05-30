package com.kasirqu.facade;

import com.kasirqu.contracts.CartContract;
import com.kasirqu.models.Barang;
import com.kasirqu.models.CartItem;
import com.kasirqu.services.create.CreateCartService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade that wraps the in-memory cart manager.
 * The GUI interacts ONLY with this class for cart operations.
 *
 * Architecture: GUI → CartFacade → CreateCartService (in-memory)
 *
 * IMPORTANT: Cart is NOT persisted to any database table.
 * It lives in memory during the application session.
 *
 * Status: ✅ Fully wired
 */
public class CartFacade implements CartContract {

    private final CreateCartService cartService;

    public CartFacade() {
        this.cartService = new CreateCartService();
    }

    /**
     * Constructor for sharing a single cart service instance.
     * Use this when TransactionFacade needs access to the same cart.
     */
    public CartFacade(CreateCartService cartService) {
        this.cartService = cartService;
    }

    // ═══════════════════════════════════════════════════════════
    // MUTATORS
    // ═══════════════════════════════════════════════════════════

    @Override
    public void addItem(Barang barang, int qty) {
        cartService.addItem(barang, qty);
    }

    @Override
    public boolean updateItemQty(int idBarang, int newQty) {
        return cartService.updateItemQty(idBarang, newQty);
    }

    @Override
    public boolean increaseQty(int idBarang, int delta) {
        return cartService.increaseQty(idBarang, delta);
    }

    @Override
    public boolean removeItem(int idBarang) {
        return cartService.removeItem(idBarang);
    }

    @Override
    public void clearCart() {
        cartService.clearCart();
    }

    // ═══════════════════════════════════════════════════════════
    // GETTERS
    // ═══════════════════════════════════════════════════════════

    @Override
    public List<CartItem> getCartItems() {
        return cartService.getCartItems();
    }

    @Override
    public int getItemCount() {
        return cartService.getItemCount();
    }

    @Override
    public boolean isEmpty() {
        return cartService.isEmpty();
    }

    // ═══════════════════════════════════════════════════════════
    // CALCULATIONS
    // ═══════════════════════════════════════════════════════════

    @Override
    public BigDecimal calculateSubtotal() {
        return cartService.calculateSubtotal();
    }

    @Override
    public int calculateTotalQty() {
        return cartService.calculateTotalQty();
    }

    /**
     * Exposes the underlying cart service for TransactionFacade integration.
     * This allows TransactionFacade to get the cart snapshot for checkout.
     */
    public CreateCartService getCartService() {
        return cartService;
    }
}
