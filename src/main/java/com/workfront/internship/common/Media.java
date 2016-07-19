package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */
public class Media {
    private int mediaID;
    private String mediaPath;
    private int productID;

    public int getMediaID() {
        return mediaID;
    }

    public Media setMediaID(int mediaID) {
        this.mediaID = mediaID;
        return this;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public Media setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }

    public int getProductID() {
        return productID;
    }

    public Media setProductID(int productID) {
        this.productID = productID;
        return this;
    }


}
