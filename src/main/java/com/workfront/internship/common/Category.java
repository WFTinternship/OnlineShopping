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

    public int getParentID() {
        return parentID;
    }

    public Category setParentID(int parentID) {
        this.parentID = parentID;
        return this;
    }

    private int parentID;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Category category = (Category) obj;

        if (getCategoryID() != category.getCategoryID()) return false;
        if (getName() != category.getName()) return false;


        return true;
    }
}
