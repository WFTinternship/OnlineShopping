package com.workfront.internship.common;

/**
 * Created by Anna Asmangulyan on 03.09.2016.
 */
public class Size {

    private int sizeId;
    private int categoryId;
    private String sizeOption;

    public int getSizeId() {
        return sizeId;
    }

    public Size setSizeId(int sizeId) {
        this.sizeId = sizeId;
        return  this;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Size setCategoryId(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getSizeOption() {
        return sizeOption;
    }

    public Size setSizeOption(String sizeOption) {
        this.sizeOption = sizeOption;
        return this;
    }
}
