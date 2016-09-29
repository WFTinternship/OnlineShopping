package com.workfront.internship.business;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 23.07.2016.
 */
@Component
public class ProductManagerImpl implements ProductManager {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private MediaManager mediaManager;


    public int createNewProduct(Product product) {
        if (!validateProduct(product))
            throw new RuntimeException("not valid product");
        int index = 0;
        index = productDao.insertProduct(product);

        return index;
    }

    public Product getProduct(int productId) {
        if (productId <= 0)
            throw new RuntimeException("not valid id");
        Product product = productDao.getProductByID(productId);
        return product;
    }

    public void updateProduct(Product product) {
        if(product == null)
            throw new RuntimeException("not valid product");
            productDao.updateProduct(product);

    }
    public void updateSaleField(int id, int discount){
        if (id <= 0 || discount <0)
            throw new RuntimeException("not valid entry");
         productDao.updateSaledField(id, discount);
    }

    @Override
    public int getQuantity(int productId, String sizeOption) {
        if (productId <= 0 || sizeOption == null || sizeOption == "")
            throw new RuntimeException("not valid id");
        return productDao.getQuantity(productId, sizeOption);
    }

    public void deleteProduct(int id) {

        productDao.deleteProductByID(id);


    }

    public List<Product> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products;
    }
    public List<Product> getSaledProducts() {
        List<Product> products = productDao.getSaledProducts();
        return products;
    }

    public List<Product> getProdactsByCategoryID(int id) {

        List<Product> products = productDao.getProdactsByCategoryID(id);
        return products;

    }

    public List<Product> getLimitedNumberOfProducts() {
        List<Product> products = productDao.getLimitedNumberOfProducts();
        return products;

    }
    public List<Product> getProducts(int categoryId, String str){
		if (categoryId <= 0 || str == null)
			throw new RuntimeException("not valid entry");
		return productDao.getProducts(categoryId, str);
	}

    public void setSizes(int productId, String sizeOption, int quantity) {
        productDao.setSizes(productId, sizeOption, quantity);
    }

    private boolean validateProduct(Product product) {
        if (product != null && product.getName() != null && product.getCategory() != null)
            return true;
        return false;
    }

    public Map<String, Integer> getSizeOptionQuantityMap(int productId) {
        return productDao.getSizeOptionQuantityMap(productId);
    }

    public void deleteProductFromProductSizeTable(int id, String option) {
        productDao.deleteProductFromProductSizeTable(id, option);
    }

    public List<String> getLikeStringsByCategory(int categoryID, String str){
        List<String> result = new ArrayList<>();

        List<Category> categories = categoryDao.getCategories(categoryID, str);

        for(Category category : categories){
			if(!result.contains(category.getName())) {
				result.add(category.getName());
			}
            List<Product> products = productDao.getProducts(category.getCategoryID(), str);
            for(Product product : products){
				if(!result.contains(product.getName())) {
					result.add(product.getName());
				}

            }
        }
        return result;
    }
    public void deleteAllProducts(){
        productDao.deleteAllProducts();
    }
}
