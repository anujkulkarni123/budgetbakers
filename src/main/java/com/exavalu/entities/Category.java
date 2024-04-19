package com.exavalu.entities;

import java.util.List;

public class Category {
    String categoryName;
    List<String> subCategories;

    public Category(String categoryName, List<String> subCategories) {
        this.categoryName = categoryName;
        this.subCategories = subCategories;
    }
}
