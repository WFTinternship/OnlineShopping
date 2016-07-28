package com.workfront.internship.business;

import com.workfront.internship.common.Media;
import com.workfront.internship.dao.DataSource;
import com.workfront.internship.dao.MediaDao;
import com.workfront.internship.dao.MediaDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Workfront on 7/28/2016.
 */
public class MediaManagerImpl implements MediaManager {

    private DataSource dataSource;
    private MediaDao mediaDao;

    public MediaManagerImpl(DataSource dataSource)throws IOException, SQLException {
        this.dataSource = dataSource;
        mediaDao = new MediaDaoImpl(dataSource);
    }
    public List<Media> getMediaByProductID(int id){

        List<Media> medias = mediaDao.getMediaByProductID(id);
        return medias;
    }
    public Media getMediaByMediaID(int id){

        Media media = mediaDao.getMediaByMediaID(id);
        return media;
    }
    public int insertMedia(Media media){

        int index = mediaDao.insertMedia(media);
        return index;

    }
    public void updateMedia(Media media){

        mediaDao.updateMedia(media);

    }
    public void deleteMediaByID(int id){

        mediaDao.deleteMediaByID(id);

    }

    public void deleteMediaByProductID(int id){

        mediaDao.deleteMediaByProductID(id);
    }

}
