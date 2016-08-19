package com.workfront.internship.dao;


import com.workfront.internship.common.Media;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MediaDaoImpl extends GeneralDao implements MediaDao {

    private static final Logger LOGGER = Logger.getLogger(MediaDao.class);
    @Autowired
    private LegacyDataSource dataSource;

    public MediaDaoImpl(LegacyDataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
    }
    @Override
    public List<Media> getMediaByProductID(int productId) {
        List<Media> medias = new ArrayList<Media>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * from medias where product_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            resultSet = preparedStatement.executeQuery();
            medias = createMediaList(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

        return medias;
    }
    @Override
    public void deleteMediaByID(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from medias where media_id = ?";
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
    public void deleteMediaByProductID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from medias where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }
    @Override
    public void deleteMediaByPath(String path) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from medias where media_path = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, path);
            preparedStatement.executeUpdate();


        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }


    }

    public int insertMedia( Media media){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int lastId = 0;
        try {
            connection = dataSource.getConnection();
            String sql = "INSERT into medias(media_path, product_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, media.getMediaPath());
            preparedStatement.setInt(2, media.getProductID());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                lastId = resultSet.getInt(1);
                media.setMediaID(lastId);
            }

        } catch ( SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }
    @Override
    public Media getMediaByMediaID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Media media = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * from medias where media_id = ?";
            media = new Media();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            media = createMedia(resultSet);


        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return media;
    }
    @Override
    public void updateMedia(Media media) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "UPDATE medias SET media_path = ? where media_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, media.getMediaPath());
            preparedStatement.setInt(2, media.getMediaID());
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
    public void deleteAllMedias(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from medias";
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
    public List<Media> getAllMedias(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Media> medias = new ArrayList<>();
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * FROM medias";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            medias = createMediaList(resultSet);

        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return medias;
    }
    private List<Media> createMediaList(ResultSet resultSet) throws SQLException {
        List<Media> medias = new ArrayList<>();
        Media media = null;
        while (resultSet.next()) {
            media = new Media();
            media.setProductID(resultSet.getInt("product_id")).
                    setMediaPath(resultSet.getString("media_path")).
                    setMediaID(resultSet.getInt("media_id"));
            medias.add(media);
        }

        return medias;
    }
    private Media createMedia(ResultSet resultSet) throws SQLException {
        Media media = null;
        while (resultSet.next()) {
            media = new Media();
            media.setProductID(resultSet.getInt("product_id")).
                    setMediaPath(resultSet.getString("media_path")).
                    setMediaID(resultSet.getInt("media_id"));
        }

        return media;
    }
}

