package com.workfront.internship.business;


import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;

import java.util.List;
import java.util.Map;

public interface ProductManager {

    int createNewProduct(Product product);
    Product getProduct(int productId);
    void updateProduct(Product product);
    void deleteProduct(int id);
    List<Product> getProdactsByCategoryID(int id);
    List<Product> getLimitedNumberOfProducts();
    List<Product> getAllProducts();
    void setSizes(int productId, String sizeOption, int quantity);
    Map<String, Integer> getSizeOptionQuantityMap(int productId);

}
