package com.numad19f.udesign;

import java.util.ArrayList;
import java.util.List;

class CategoryItem {
    String categoryName;
    int photoId;

    CategoryItem(String name, int photoId) {
        this.categoryName = name;
        this.photoId = photoId;
    }

    private List<CategoryItem> categories;

    private void initializeData() {
        categories = new ArrayList<>();
        categories.add(new CategoryItem("Bedroom Designs", R.drawable.bedroom));
        categories.add(new CategoryItem("Living Room Designs", R.drawable.living));
        categories.add(new CategoryItem("Kitchen Designs", R.drawable.kitchen));
        categories.add(new CategoryItem("Kids Room Designs", R.drawable.kids));
        categories.add(new CategoryItem("Patio Designs",  R.drawable.patio));
        categories.add(new CategoryItem("Garden Designs", R.drawable.garden));
    }
}
