package com.kasirqu.exceptions;

/**
 * Thrown when attempting to create a product with a kode_barang
 * that already exists in the database.
 */
public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String kodeBarang) {
        super("Produk dengan kode '" + kodeBarang + "' sudah ada di database.");
    }
}
