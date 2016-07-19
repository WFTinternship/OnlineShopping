package com.workfront.internship.dao;

import com.workfront.internship.common.Media;

import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public interface MediaDao {
    List<Media> getMediaByProductID(int productId);
    Media getMediaByMediaID(int mediaId);
    int insertMedia(Media media);
    void updateMedia(Media media);
    void deleteMediaByID(int id);
    void deleteMediaByPath(String path);
    void deleteMediaByProductID(int id);
    List<Media> getAllMedias();
    void deleteAllMedias();
}
