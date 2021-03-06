package com.workfront.internship.dao;

import com.workfront.internship.common.Category;

import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface CategoryDao {

    Category getCategoryByID(int catId);

    Category getCategoryByParentIDANDCategoryName(int parentId, String name);

    int insertCategory(Category category);

    void deleteCategoryByID(int id);

    void deleteCategoryByParentID(int id);

    void updateCategory(Category category);

    void deleteAllCategories();

    List<Category> getAllCategories();

    List<Category> getChildCategories(int parentID);

    List<Category> getCategoriesByParentID(int id);

    List<Category> getCategories(int parentId, String str);

}
