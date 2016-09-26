package com.workfront.internship.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.RangeExceptionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class ProductDaoImpl extends GeneralDao implements ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDao.class);
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MediaDao mediaDao;
    @Autowired
    private CategoryDao categoryDao;
/*
    public ProductDaoImpl(LegacyDataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
    }*/

    @Override
    public Product getProductByID(int productId) {
        Product product = new Product();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * FROM products left join categories on products.category_id = categories.category_id" +
                    " where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            resultSet = preparedStatement.executeQuery();
            product = createProduct(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return product;
    }
    @Override
    public int getQuantity(int productId, String sizeOption){
        int quantity = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * FROM product_size  where product_id = ? AND size_option = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setString(2, sizeOption);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                quantity = resultSet.getInt("quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return quantity;

    }
    public List<Product> getLimitedNumberOfProducts(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM products LIMIT 5";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            products = createProductList(resultSet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return products;

    }
    public List<Product> getSaledProducts(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM products where saled > 0";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            products = createProductList(resultSet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return products;

    }
    public Map<String, Integer> getSizeOptionQuantityMap(int productId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String, Integer> sizeOptionQuantityMap = new HashMap<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM product_size where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, productId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sizeOptionQuantityMap.put(resultSet.getString("size_option"), resultSet.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return sizeOptionQuantityMap;

    }


    private Product createProduct(ResultSet resultSet) throws SQLException, IOException {
        Product product = null;
        Category category = null;
        List<Media> medias = new ArrayList<Media>();

        while (resultSet.next()) {
            category = new Category();
            category = category.setCategoryID(resultSet.getInt("category_id")).
                    setName(resultSet.getString("category_name"));
            medias = mediaDao.getMediaByProductID(resultSet.getInt("product_id"));
            product = new Product();
            product.setProductID(resultSet.getInt("product_id")).
                    setName(resultSet.getString("name")).
                    setPrice(resultSet.getDouble("price")).
                    setDescription(resultSet.getString("description")).
                    setShippingPrice(resultSet.getDouble("shipping_price")).
                    setCategory(category).
                    setSaled(resultSet.getInt("saled")).
                    setMedias(medias).setSizeOptionQuantity(getSizeOptionQuantityMap(resultSet.getInt("product_id")));


        }
        return product;
    }

    @Override
    public int insertProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int lastId = 0;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into products(name, price, description, shipping_price, category_id)" +
                    " VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDouble(4, product.getShippingPrice());
            preparedStatement.setInt(5, product.getCategory().getCategoryID());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                product.setProductID(lastId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;

    }


    @Override
    public void updateProduct(Product product) {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            updateProduct(connection, product);
            Set<Map.Entry<String, Integer>> set = product.getSizeOptionQuantity().entrySet();
            for (Map.Entry<String, Integer> entry : set) {
                updateProductQuantity(connection, product.getProductID(), entry.getKey(), entry.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("can not get a connection");
        }

    }

    @Override
    public void updateProduct(Connection connection, Product product) {
        PreparedStatement preparedStatement = null;
        try {

            String sql = "UPDATE products SET name = ?, price = ?, description = ?," +
                    " shipping_price = ?, category_id = ? where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setDouble(4, product.getShippingPrice());
            preparedStatement.setInt(5, product.getCategory().getCategoryID());
            preparedStatement.setInt(6, product.getProductID());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void updateSaledField(int id, int discount){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String sql = "UPDATE products SET saled = ? where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, discount);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void updateProductQuantity(Connection connection, int productId, String sizeOption, int quantity){
        PreparedStatement preparedStatement = null;
        try {

            String sql = "UPDATE product_size SET quantity = ? where product_id = ? AND size_option = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            preparedStatement.setString(3, sizeOption);

            preparedStatement.executeUpdate();

        } catch(MysqlDataTruncation e) {
        e.printStackTrace();
        LOGGER.error("Negative number!");
            throw new RuntimeException("Negative number!");
    }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void deleteProductByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from products where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void deleteProductFromProductSizeTable(int id, String sizeOption){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from product_size where product_id = ? AND size_option = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, sizeOption);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }
    @Override
    public void deleteProductByName(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from products where  name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }

    @Override
    public void deleteAllProducts() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from products";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(null, preparedStatement, connection);
        }

    }

    @Override
    public List<Product> getAllProducts() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM products";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            products = createProductList(resultSet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return products;
    }
    public List<Product> getProdactsByCategoryID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM products where category_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            products = createProductList(resultSet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return products;

    }
    public void setSizes(int productid, String sizeOption, int quantity){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int lastId = 0;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into product_size(product_id, size_option, quantity)" +
                    " VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, productid);
            preparedStatement.setString(2, sizeOption);
            preparedStatement.setInt(3, quantity);

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    private List<Product> createProductList(ResultSet resultSet) throws SQLException, IOException {
        List<Product> products = new ArrayList<>();
        Product product = null;
        Category category;
        while (resultSet.next()) {
            product = new Product();
            category = categoryDao.getCategoryByID(resultSet.getInt("category_id"));
            product.setProductID(resultSet.getInt("product_id")).
                    setName(resultSet.getString("name")).
                    setCategory(category).
                    setDescription(resultSet.getString("description")).
                    setPrice(resultSet.getDouble("price")).
                    setSaled(resultSet.getInt("saled")).
                    setShippingPrice(resultSet.getDouble("shipping_price"));
            products.add(product);
        }

        return products;
    }
}
