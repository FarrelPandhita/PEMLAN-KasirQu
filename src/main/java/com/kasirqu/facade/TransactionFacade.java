package com.kasirqu.facade;

import com.kasirqu.models.Transaksi;
import com.kasirqu.models.DetailTransaksi;
import com.kasirqu.contracts.TransactionContract;
import java.util.List;

public class TransactionFacade implements TransactionContract {
    
    // TODO: Inject CRUD Services here
    
    @Override
    public int createTransaction(Transaksi transaksi, List<DetailTransaksi> items) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Transaksi> getTransactionHistory(int limit, int offset) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
