package app.kibbeh.model;

import java.util.ArrayList;

/**
 * Created by archi on 11/10/2016.
 */

public class HomeModel
{
    String depName,depId,StorId;
    ArrayList<Product> productArray;

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getStorId() {
        return StorId;
    }

    public void setStorId(String storId) {
        StorId = storId;
    }

    public ArrayList<Product> getProductArray() {
        return productArray;
    }

    public void setProductArray(ArrayList<Product> productArray) {
        this.productArray = productArray;
    }
}
