package com.workfront.internship.business;


import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;

import java.util.List;

public interface ProductManager {

    int insertProduct(Product product);
    Product getProductByID(int productId);
    void updateProduct(Product product);
    void deleteProductByID(int id);
    void deleteAllProducts();
    List<Product> getAllProducts();
    List<Product> getProdactsByCategoryID(int id);
    Category getCategoryByID(int catId);
    int insertCategory(Category category);
    void deleteCategoryByID(int id);
    void updateCategory(Category category);
    void deleteAllCategories();
    List<Category> getAllCategories();
    List<Media> getMediaByProductID(int productId);
    Media getMediaByMediaID(int mediaId);
    int insertMedia(Media media);
    void updateMedia(Media media);
    void deleteMediaByID(int id);
    void deleteMediaByProductID(int id);
    List<Media> getAllMedias();
    void deleteAllMedias();
}
