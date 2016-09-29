package com.workfront.internship.dao;

import com.workfront.internship.common.Product;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface ProductDao {

    Product getProductByID(int productId);

    int getQuantity(int productId, String sizeOption);

    int insertProduct(Product product);

    void updateProduct(Product product);

    void updateProductQuantity(Connection connection, int productId, String sizeOption, int quantity);

    void deleteProductByID(int id);

    void deleteProductFromProductSizeTable(int id, String option);

    void deleteProductByName(String name);

    void updateProduct(Connection connection, Product product);

    void updateSaledField(int id, int discount);

    void deleteAllProducts();

    List<Product> getAllProducts();

    List<Product> getProducts(int categoryId, String str);

    List<Product> getSaledProducts();

    List<Product> getProdactsByCategoryID(int id);

    List<Product> getLimitedNumberOfProducts();

    void setSizes(int productId, String sizeOption, int quantity);

    Map<String, Integer> getSizeOptionQuantityMap(int productId);


}
