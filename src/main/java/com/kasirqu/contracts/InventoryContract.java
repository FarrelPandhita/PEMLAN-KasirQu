package com.kasirqu.contracts;

import com.kasirqu.models.Barang;
import java.util.List;

public interface InventoryContract {
    boolean createProduct(Barang barang);
    Barang getProductByCode(String code);
    List<Barang> getProducts(int limit, int offset);
    List<Barang> searchProduct(String keyword);
    boolean updateProduct(Barang barang);
    boolean deleteProduct(int idBarang);
}
