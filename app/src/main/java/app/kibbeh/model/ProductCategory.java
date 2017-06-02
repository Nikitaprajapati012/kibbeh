package app.kibbeh.model;

/**
 * Created by archi on 8/29/2016.
 */

public class ProductCategory {
    String categoryId,categoryName;

    public ProductCategory(String strID, String strCategory) {
        categoryId = strID;
        categoryName = strCategory;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
