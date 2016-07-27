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
   private CategoryDao categoryDao;
   private MediaDao mediaDao;

   public ProductManagerImpl(DataSource dataSource)throws IOException, SQLException {
       this.dataSource = dataSource;
       productDao = new ProductDaoImpl(dataSource);
       categoryDao = new CategoryDaoImpl(dataSource);
       mediaDao = new MediaDaoImpl(dataSource);
   }
   public int insertProduct(Product product){
       int index = 0;

          index = productDao.insertProduct(product);
       if(index > 0 && !(product.getMedias().isEmpty()))
           for(int i = 0; i <product.getMedias().size(); i++)
               mediaDao.insertMedia(product.getMedias().get(i));

       return index;
   }
   public Product getProductByID(int productId){

       Product product = productDao.getProductByID(productId);
       return product;
   }
   public void updateProduct(Product product){

       productDao.updateProduct(product);

       List<Media> oldMedias = mediaDao.getMediaByProductID(product.getProductID());
       List<Media> newMedias = product.getMedias();
       for(int i = 0; i < newMedias.size(); i ++)
           if(!oldMedias.contains(newMedias.get(i)))
               mediaDao.insertMedia(newMedias.get(i));
       for(int i = 0; i < oldMedias.size(); i ++)
           if(!newMedias.contains(oldMedias.get(i)))
               mediaDao.deleteMediaByProductID(oldMedias.get(i).getMediaID());

   }
   public void deleteProductByID(int id){

       productDao.deleteProductByID(id);

   }
   public void deleteAllProducts(){

       productDao.deleteAllProducts();

   }
   public List<Product> getAllProducts(){

       List<Product> products = productDao.getAllProducts();
       return products;

   }
   public List<Product> getProdactsByCategoryID(int id){

       List<Product> products = productDao.getProdactsByCategoryID(id);
       return products;

   }
   public Category getCategoryByID(int id){

       Category category = categoryDao.getCategoryByID(id);
       return category;

   }
   public int insertCategory(Category category){

       int index = categoryDao.insertCategory(category);
       return index;
   }
   public void deleteCategoryByID(int id){

       categoryDao.deleteCategoryByID(id);

   }
   public void updateCategory(Category category){

       categoryDao.updateCategory(category);

   }
   public void deleteAllCategories(){

       categoryDao.deleteAllCategories();

   }
   public List<Category> getAllCategories(){

       List<Category> categories = categoryDao.getAllCategories();
       return  categories;
   }
   public List<Media> getMediaByProductID(int id){

       List<Media> medias = mediaDao.getMediaByProductID(id);
       return medias;
   }
   public Media getMediaByMediaID(int id){

       Media media = mediaDao.getMediaByMediaID(id);
       return media;
   }
   public int insertMedia(Media media){

       int index = mediaDao.insertMedia(media);
       return index;

   }
   public void updateMedia(Media media){

       mediaDao.updateMedia(media);

   }
   public void deleteMediaByID(int id){

       mediaDao.deleteMediaByID(id);

   }

   public void deleteMediaByProductID(int id){

       mediaDao.deleteMediaByProductID(id);
   }
   public List<Media> getAllMedias(){

       List<Media> medias = mediaDao.getAllMedias();
       return medias;

   }
   public void deleteAllMedias(){

       mediaDao.deleteAllMedias();

   }
}
