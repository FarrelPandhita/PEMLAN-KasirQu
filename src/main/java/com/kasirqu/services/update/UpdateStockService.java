package com.kasirqu.services.update;

public class UpdateStockService {

    public boolean validateStockUpdate(
            int currentStock,
            int qtyChange
    ) {

        int newStock =
                currentStock + qtyChange;

        return newStock >= 0;
    }
}