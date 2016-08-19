package com.workfront.internship.business;

import com.workfront.internship.common.Media;
import com.workfront.internship.dao.LegacyDataSource;
import com.workfront.internship.dao.MediaDao;
import com.workfront.internship.dao.MediaDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Anna Asmangulyan on 7/28/2016.
 */
@Component
public class MediaManagerImpl implements MediaManager {

    @Autowired
    private LegacyDataSource dataSource;
    @Autowired
    private MediaDao mediaDao;

    public MediaManagerImpl(LegacyDataSource dataSource)throws IOException, SQLException {
        this.dataSource = dataSource;
        mediaDao = new MediaDaoImpl(dataSource);
    }
    public List<Media> getMediaByProductID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");

        List<Media> medias = mediaDao.getMediaByProductID(id);
        return medias;
    }
    public Media getMediaByMediaID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");

        Media media = mediaDao.getMediaByMediaID(id);
        return media;
    }
    public int insertMedia(Media media){
        if(!validateMedia(media))
            throw new RuntimeException("invalid id");

        int index = mediaDao.insertMedia(media);
        return index;

    }
    public void updateMedia(Media media){
        if(!validateMedia(media))
            throw new RuntimeException("invalid id");

        mediaDao.updateMedia(media);

    }
    public void deleteMediaByID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");

        mediaDao.deleteMediaByID(id);

    }

    public void deleteMediaByProductID(int id){
        if(id <= 0)
            throw new RuntimeException("invalid id");

        mediaDao.deleteMediaByProductID(id);
    }
    private boolean validateMedia(Media media){
        if(media != null && media.getProductID() >=0 && media.getMediaPath() != null)
            return true;
        return false;
    }

}
