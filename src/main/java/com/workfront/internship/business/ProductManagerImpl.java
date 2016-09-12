package com.workfront.internship.business;

import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 23.07.2016.
 */
@Component
public class ProductManagerImpl implements ProductManager{

   @Autowired
   private ProductDao productDao;
   @Autowired
   private MediaManager mediaManager;


   public int createNewProduct(Product product){
       if(!validateProduct(product))
           throw new RuntimeException("not valid product");
       int index = 0;
       index = productDao.insertProduct(product);
       if(index > 0 && product.getMedias() !=null && !(product.getMedias().isEmpty()))
           for(int i = 0; i <product.getMedias().size(); i++)
               mediaManager.insertMedia(product.getMedias().get(i));

       return index;
   }
   public Product getProduct(int productId){
       if(productId<=0)
           throw new RuntimeException("not valid id");
       Product product = productDao.getProductByID(productId);
       return product;
   }
   public void updateProduct(Product product){

       productDao.updateProduct(product);

       List<Media> oldMedias = mediaManager.getMediaByProductID(product.getProductID());
       List<Media> newMedias = product.getMedias();
       for(int i = 0; i < newMedias.size(); i ++)
           if(!oldMedias.contains(newMedias.get(i)))
               mediaManager.insertMedia(newMedias.get(i));
       for(int i = 0; i < oldMedias.size(); i ++)
           if(!newMedias.contains(oldMedias.get(i)))
               mediaManager.deleteMediaByID(oldMedias.get(i).getMediaID());

   }
   public void deleteProduct(int id){

       productDao.deleteProductByID(id);


   }
   public  List<Product> getAllProducts(){
       List<Product> products = productDao.getAllProducts();
       return products;
   }
   public List<Product> getProdactsByCategoryID(int id){

       List<Product> products = productDao.getProdactsByCategoryID(id);
       return products;

   }
    public List<Product> getLimitedNumberOfProducts(){
        List<Product> products = productDao.getLimitedNumberOfProducts();
        return products;

    }
   public void setSizes(int productId, String sizeOption, int quantity){
        productDao.setSizes(productId, sizeOption, quantity);
   }
   private boolean validateProduct(Product product){
       if(product!=null && product.getName() != null && product.getCategory() != null)
           return true;
       return false;
   }
   public Map<String, Integer> getSizeOptionQuantityMap(int productId){
       return productDao.getSizeOptionQuantityMap(productId);
   }


}
