package com.workfront.internship.business;

import com.workfront.internship.common.Category;
import com.workfront.internship.dao.CategoryDao;
import com.workfront.internship.dao.CategoryDaoImpl;
import com.workfront.internship.dao.DataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/28/2016.
 */
public class CategoryManagerImpl implements CategoryManager {
    private CategoryDao categoryDao;
    private DataSource dataSource;

    public CategoryManagerImpl(DataSource dataSource)throws IOException, SQLException {
        this.dataSource = dataSource;
        categoryDao = new CategoryDaoImpl(dataSource);

    }

    public Category getCategoryByID(int id){
        if(id <=0)
            throw new RuntimeException("not a valid categoryId");
        Category category = categoryDao.getCategoryByID(id);
        return category;

    }
    public int createNewCategory(Category category){
        if(!validateCategory(category))
            throw new RuntimeException("not a valid category");
        int index = categoryDao.insertCategory(category);
        return index;
    }
    public void deleteCategory(int id){
        if(id <=0)
            throw new RuntimeException("not a valid categoryId");
        categoryDao.deleteCategoryByID(id);

    }
    public void updateCategory(Category category){
        if(!validateCategory(category))
            throw new RuntimeException("not a valid category");
        categoryDao.updateCategory(category);

    }

    public List<Category> getAllCategories(){

        List<Category> categories = categoryDao.getAllCategories();
        return  categories;
    }
    private boolean validateCategory(Category category){
        if(category!=null && category.getName()!=null)
            return true;
            return false;
    }
}
