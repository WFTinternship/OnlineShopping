package com.workfront.internship.business;

import com.workfront.internship.common.Media;

import java.util.List;

/**
 * Created by Workfront on 7/28/2016.
 */
public interface MediaManager {
    List<Media> getMediaByProductID(int productId);
    Media getMediaByMediaID(int mediaId);
    int insertMedia(Media media);
    void updateMedia(Media media);
    void deleteMediaByID(int id);
    void deleteMediaByProductID(int id);
}
