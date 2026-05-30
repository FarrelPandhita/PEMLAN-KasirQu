package com.kasirqu.contracts;

import com.kasirqu.models.CartItem;
import com.kasirqu.models.CheckoutResult;
import com.kasirqu.models.Transaksi;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Facade contract for transaction operations.
 * The GUI calls ONLY these methods via TransactionFacade.
 */
public interface TransactionContract {

    // ── CREATE (checkout) ────────────────────────────────────
    CheckoutResult checkout(List<CartItem> cartItems, String namaOperator,
                            BigDecimal diskon, BigDecimal pajak,
                            String metodeBayar, String catatan) throws SQLException;

    CheckoutResult checkout(List<CartItem> cartItems, String namaOperator) throws SQLException;

    // ── READ (history) ───────────────────────────────────────
    // TODO: Wire ReadTransactionService when implemented
    List<Transaksi> getTransactionHistory(int limit, int offset);
}
