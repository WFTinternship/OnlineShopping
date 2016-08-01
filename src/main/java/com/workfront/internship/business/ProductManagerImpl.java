package com.workfront.internship.business;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.dao.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 23.07.2016.
 */
public class ProductManagerImpl implements ProductManager{

   private DataSource dataSource;
   private ProductDao productDao;

   private MediaManager mediaManager;

   public ProductManagerImpl(DataSource dataSource)throws IOException, SQLException {
       this.dataSource = dataSource;
       productDao = new ProductDaoImpl(dataSource);

       mediaManager = new MediaManagerImpl(dataSource);
   }
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
   public List<Product> getProdactsByCategoryID(int id){

       List<Product> products = productDao.getProdactsByCategoryID(id);
       return products;

   }
   private boolean validateProduct(Product product){
       if(product!=null && product.getName() != null && product.getCategory() != null)
           return true;
       return false;
   }


}
