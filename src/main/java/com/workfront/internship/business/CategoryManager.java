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
    void deleteCategoryByParentID(int id);
    List<Category> getAllCategories();
    List<Category> getChildCategories(int parentId);
    List<Category> getCategoriesByParentID(int id);
    Category getCategoryByParentIDANDCategoryName(int parentId, String name);
    void deleteAllCategories();
}
