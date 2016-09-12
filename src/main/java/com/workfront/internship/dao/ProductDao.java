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
     int insertProduct(Product product);
     void updateProduct(Product product);
     void deleteProductByID(int id);
     void deleteProductByName(String name);
     void updateProduct(Connection connection, Product product);
     void deleteAllProducts();
     List<Product> getAllProducts();
     List<Product> getProdactsByCategoryID(int id);
     List<Product> getLimitedNumberOfProducts();
     void setSizes(int productId, String sizeOption, int quantity);
     Map<String, Integer> getSizeOptionQuantityMap(int productId);

}
