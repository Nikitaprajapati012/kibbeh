package app.kibbeh.model;

/**
 * Created by archi on 8/30/2016.
 */

public class ProductSubCategory {
    String subCatid,subCatName,subCatCategoryId;

    public ProductSubCategory(String strCatId, String strSubCatName, String strCatId1) {
        subCatid = strCatId;
        subCatName = strSubCatName;
        subCatCategoryId = strCatId1;
    }

    public String getSubCatid() {
        return subCatid;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public String getSubCatCategoryId() {
        return subCatCategoryId;
    }
}
