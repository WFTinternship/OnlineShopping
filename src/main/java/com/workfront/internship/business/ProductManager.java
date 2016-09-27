package com.workfront.internship.business;


import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ProductManager {

    int createNewProduct(Product product);

    Product getProduct(int productId);

    void updateProduct(Product product);

    void updateSaleField(int id, int discount);

    void deleteProduct(int id);

    List<Product> getProdactsByCategoryID(int id);

    List<Product> getSaledProducts();

    List<Product> getLimitedNumberOfProducts();

    List<Product> getAllProducts();

    void setSizes(int productId, String sizeOption, int quantity);

    int getQuantity(int productId, String sizeOption);

    void deleteProductFromProductSizeTable(int id, String option);

    Map<String, Integer> getSizeOptionQuantityMap(int productId);

    List<String> getLikeStringsByCategory(int categoryID, String str);


    List<Product> getProducts(int categoryId, String str);

}
