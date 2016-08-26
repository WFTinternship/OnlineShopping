package com.workfront.internship.business;

import com.workfront.internship.common.Category;

import java.util.List;

/**
 * Created by Workfront on 7/28/2016.
 */
public interface CategoryManager {

    int createNewCategory(Category category);
    Category getCategoryByID(int catId);
    void updateCategory(Category category);
    void deleteCategory(int id);
    List<Category> getAllCategories();
    List<Category> getCategoriesByParentID(int id);
}
