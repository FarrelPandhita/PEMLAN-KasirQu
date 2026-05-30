package com.kasirqu.models;

import java.math.BigDecimal;

/**
 * Represents a single item in the in-memory cart.
 * Maps a Barang (product) to a desired purchase quantity.
 * This is a transient DTO — NOT persisted to any database table.
 */
public class CartItem {

    private Barang barang;
    private int qty;

    public CartItem() {}

    public CartItem(Barang barang, int qty) {
        this.barang = barang;
        this.qty = qty;
    }

    // ─── Getters & Setters ───────────────────────────────────

    public Barang getBarang() { return barang; }
    public void setBarang(Barang barang) { this.barang = barang; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    // ─── Computed ────────────────────────────────────────────

    /**
     * Calculates subtotal = harga × qty.
     * Returns BigDecimal.ZERO if barang or harga is null.
     */
    public BigDecimal getSubtotal() {
        if (barang == null || barang.getHarga() == null) {
            return BigDecimal.ZERO;
        }
        return barang.getHarga().multiply(BigDecimal.valueOf(qty));
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "barang=" + (barang != null ? barang.getNamaBarang() : "null") +
                ", qty=" + qty +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
