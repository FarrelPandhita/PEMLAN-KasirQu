package com.kasirqu.contracts;

import com.kasirqu.models.Transaksi;
import com.kasirqu.models.DetailTransaksi;
import java.util.List;

public interface TransactionContract {
    int createTransaction(Transaksi transaksi, List<DetailTransaksi> items);
    List<Transaksi> getTransactionHistory(int limit, int offset);
}
