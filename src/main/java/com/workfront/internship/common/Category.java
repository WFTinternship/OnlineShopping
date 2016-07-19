package com.workfront.internship.common;

/**
 * Created by Administrator on 01.07.2016.
 */

/**
 * Created by Administrator on 29.06.2016.
 */
public class Category {

    private int categoryID;
    private String name;

    public int getCategoryID() {
        return categoryID;
    }

    public Category setCategoryID(int categoryID) {

        this.categoryID = categoryID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

}

