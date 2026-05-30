package com.kasirqu.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Immutable result object returned after a successful checkout.
 * The GUI uses this to display the invoice/receipt.
 */
public class CheckoutResult {

    private final int idTransaksi;
    private final String kodeTransaksi;
    private final String namaOperator;
    private final Timestamp tanggalTransaksi;
    private final BigDecimal subtotal;
    private final BigDecimal diskon;
    private final BigDecimal pajak;
    private final BigDecimal grandTotal;
    private final String metodeBayar;
    private final String catatan;
    private final List<CartItem> items;

    public CheckoutResult(int idTransaksi, String kodeTransaksi, String namaOperator,
                          Timestamp tanggalTransaksi, BigDecimal subtotal, BigDecimal diskon,
                          BigDecimal pajak, BigDecimal grandTotal, String metodeBayar,
                          String catatan, List<CartItem> items) {
        this.idTransaksi = idTransaksi;
        this.kodeTransaksi = kodeTransaksi;
        this.namaOperator = namaOperator;
        this.tanggalTransaksi = tanggalTransaksi;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.pajak = pajak;
        this.grandTotal = grandTotal;
        this.metodeBayar = metodeBayar;
        this.catatan = catatan;
        this.items = items;
    }

    // ─── Getters ─────────────────────────────────────────────

    public int getIdTransaksi() { return idTransaksi; }
    public String getKodeTransaksi() { return kodeTransaksi; }
    public String getNamaOperator() { return namaOperator; }
    public Timestamp getTanggalTransaksi() { return tanggalTransaksi; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiskon() { return diskon; }
    public BigDecimal getPajak() { return pajak; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public String getMetodeBayar() { return metodeBayar; }
    public String getCatatan() { return catatan; }
    public List<CartItem> getItems() { return items; }
}
