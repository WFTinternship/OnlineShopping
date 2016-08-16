package com.workfront.internship.dao;

import com.workfront.internship.common.Category;

import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface CategoryDao {

    Category getCategoryByID(int catId);
    int insertCategory(Category category);
    void deleteCategoryByID(int id);
    void updateCategory(Category category);
    void deleteAllCategories();
    List<Category> getAllCategories();
    List<Category> getCategoriesByParentID(int id);
}
