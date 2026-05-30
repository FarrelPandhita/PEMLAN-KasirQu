package com.kasirqu.services.create;

import com.kasirqu.models.Barang;
import com.kasirqu.models.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory cart manager for the cashier workflow.
 *
 * IMPORTANT: The cart is NOT persisted to any database table.
 * It lives entirely in application memory during the session.
 *
 * Thread-safe via synchronized methods.
 *
 * Lifecycle:
 *   User adds item     → stored in memory
 *   Checkout success   → items consumed, cart cleared
 *   Checkout failed    → cart preserved (items remain)
 *   Clear cart          → memory reset
 *
 * Ownership: CREATE Developer
 */
public class CreateCartService {

    /** The in-memory cart item list */
    private final List<CartItem> cartItems;

    public CreateCartService() {
        this.cartItems = new ArrayList<>();
    }

    // ═══════════════════════════════════════════════════════════
    // ADD ITEM
    // ═══════════════════════════════════════════════════════════

    /**
     * Adds a product to the cart. If the same product (by id_barang) already
     * exists in the cart, the quantities are merged instead of creating a duplicate.
     *
     * @param barang the product to add
     * @param qty    the quantity to add (must be > 0)
     * @throws IllegalArgumentException if barang is null or qty <= 0
     */
    public synchronized void addItem(Barang barang, int qty) {
        if (barang == null) {
            throw new IllegalArgumentException("Produk tidak boleh kosong.");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        }

        // ── Merge if product already in cart ─────────────────
        for (CartItem item : cartItems) {
            if (item.getBarang().getIdBarang() == barang.getIdBarang()) {
                item.setQty(item.getQty() + qty);
                return;
            }
        }

        // ── New entry ────────────────────────────────────────
        cartItems.add(new CartItem(barang, qty));
    }

    // ═══════════════════════════════════════════════════════════
    // UPDATE QUANTITY
    // ═══════════════════════════════════════════════════════════

    /**
     * Sets the quantity of a specific cart item by product ID.
     * If the new quantity is 0 or less, the item is automatically removed.
     *
     * @param idBarang the product ID to update
     * @param newQty   the new quantity
     * @return true if the item was found and updated/removed
     */
    public synchronized boolean updateItemQty(int idBarang, int newQty) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getBarang().getIdBarang() == idBarang) {
                if (newQty <= 0) {
                    cartItems.remove(i);
                } else {
                    item.setQty(newQty);
                }
                return true;
            }
        }
        return false; // Item not found in cart
    }

    // ═══════════════════════════════════════════════════════════
    // INCREASE QUANTITY
    // ═══════════════════════════════════════════════════════════

    /**
     * Increases quantity of an existing cart item by a delta.
     *
     * @param idBarang the product ID
     * @param delta    the amount to increase (must be > 0)
     * @return true if item was found and quantity increased
     */
    public synchronized boolean increaseQty(int idBarang, int delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("Delta harus lebih dari 0.");
        }
        for (CartItem item : cartItems) {
            if (item.getBarang().getIdBarang() == idBarang) {
                item.setQty(item.getQty() + delta);
                return true;
            }
        }
        return false;
    }

    // ═══════════════════════════════════════════════════════════
    // REMOVE ITEM
    // ═══════════════════════════════════════════════════════════

    /**
     * Removes a specific item from the cart by product ID.
     *
     * @param idBarang the product ID to remove
     * @return true if the item was found and removed
     */
    public synchronized boolean removeItem(int idBarang) {
        return cartItems.removeIf(item -> item.getBarang().getIdBarang() == idBarang);
    }

    // ═══════════════════════════════════════════════════════════
    // CLEAR CART
    // ═══════════════════════════════════════════════════════════

    /**
     * Removes all items from the cart (memory reset).
     */
    public synchronized void clearCart() {
        cartItems.clear();
    }

    // ═══════════════════════════════════════════════════════════
    // GETTERS
    // ═══════════════════════════════════════════════════════════

    /**
     * Returns an unmodifiable snapshot of the current cart items.
     * Callers cannot modify the returned list directly.
     *
     * @return unmodifiable list of CartItem
     */
    public synchronized List<CartItem> getCartItems() {
        return Collections.unmodifiableList(new ArrayList<>(cartItems));
    }

    /**
     * Returns the number of distinct items (lines) in the cart.
     *
     * @return number of cart lines
     */
    public synchronized int getItemCount() {
        return cartItems.size();
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if no items in cart
     */
    public synchronized boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // ═══════════════════════════════════════════════════════════
    // CALCULATIONS
    // ═══════════════════════════════════════════════════════════

    /**
     * Calculates the total subtotal across all cart items.
     * (Sum of each item's harga × qty)
     *
     * @return the grand subtotal
     */
    public synchronized BigDecimal calculateSubtotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * Calculates the total number of units in the cart.
     *
     * @return total quantity across all items
     */
    public synchronized int calculateTotalQty() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQty();
        }
        return total;
    }
}
