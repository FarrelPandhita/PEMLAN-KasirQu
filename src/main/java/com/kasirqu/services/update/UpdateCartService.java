package com.kasirqu.services.update;

public class UpdateCartService {

    public boolean updateItemQty(
            int idBarang,
            int qty
    ) {

        if (idBarang <= 0) {
            return false;
        }

        return qty > 0;
    }
}