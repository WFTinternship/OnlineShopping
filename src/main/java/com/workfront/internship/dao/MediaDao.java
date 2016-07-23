package com.workfront.internship.dao;

import java.sql.Connection;;
import com.workfront.internship.common.Media;

import java.util.List;

public interface MediaDao {
    List<Media> getMediaByProductID(int productId);
    Media getMediaByMediaID(int mediaId);
    int insertMedia(Media media);
    int insertMedia(Connection connection, Media media);
    void updateMedia(Media media);
    void deleteMediaByID(int id);
    void deleteMediaByPath(String path);
    void deleteMediaByProductID(int id);
    List<Media> getAllMedias();
    void deleteAllMedias();
}
