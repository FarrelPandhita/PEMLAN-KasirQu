package com.kasirqu.exceptions;

/**
 * Thrown when the cart is empty but checkout is attempted.
 */
public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Keranjang belanja kosong. Tidak dapat melakukan checkout.");
    }
}
