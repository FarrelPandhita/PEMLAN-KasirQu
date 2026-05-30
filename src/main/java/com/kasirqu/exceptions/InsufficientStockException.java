package com.kasirqu.exceptions;

/**
 * Thrown when checkout detects that requested quantity
 * exceeds available stock for a product.
 */
public class InsufficientStockException extends RuntimeException {

    private final int idBarang;
    private final String namaBarang;
    private final int requestedQty;
    private final int availableStock;

    public InsufficientStockException(int idBarang, String namaBarang,
                                      int requestedQty, int availableStock) {
        super("Stok tidak cukup untuk '" + namaBarang + "'. " +
              "Diminta: " + requestedQty + ", Tersedia: " + availableStock);
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.requestedQty = requestedQty;
        this.availableStock = availableStock;
    }

    public int getIdBarang() { return idBarang; }
    public String getNamaBarang() { return namaBarang; }
    public int getRequestedQty() { return requestedQty; }
    public int getAvailableStock() { return availableStock; }
}
