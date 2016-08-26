package com.workfront.internship.dao;

import com.workfront.internship.common.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDaoImpl extends GeneralDao implements CategoryDao {

    private static final Logger LOGGER = Logger.getLogger(CategoryDao.class);
    @Autowired
    private DataSource dataSource;

    @Override
    public Category getCategoryByID(int categoryId) {
        Category category = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * from categories where category_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                category = new Category();
                String catname = resultSet.getString("category_name");
                category.setCategoryID(categoryId);
                category.setName(catname);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return category;
    }
    public Category getMainCategoryByName(String name){
        Category category = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * from categories where category_name =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                category = new Category();
                int id = resultSet.getInt("category_id");
                int parentId = resultSet.getInt("parent_id");
                category.setCategoryID(id);
                category.setName(name).setParentID(parentId);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return category;

    }

    @Override
    public int insertCategory(Category category) {
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into categories(category_name, parent_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getParentID());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                category.setCategoryID(lastId);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }
    @Override
    public void updateCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "UPDATE categories SET category_name = ? where category_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getCategoryID());
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw  new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void deleteCategoryByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from categories where category_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();


        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

    }

    @Override
    public void deleteAllCategories(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from categories";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    @Override
    public List<Category> getAllCategories(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Category> categories = new ArrayList<Category>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM categories";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            categories = createCategoryList(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw  new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return categories;
    }
    //TODO testing
    public List<Category> getCategoriesByParentID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Category> categories = new ArrayList<Category>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM categories where parent_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            categories = createCategoryList(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw  new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return categories;
    }

    private List<Category> createCategoryList(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<Category>();
        Category category = null;
        while (resultSet.next()) {
            category = new Category();
            category.setCategoryID(resultSet.getInt("category_id")).setName(resultSet.getString("category_name")).setParentID(resultSet.getInt("parent_id"));
            categories.add(category);
        }

        return categories;
    }

}
