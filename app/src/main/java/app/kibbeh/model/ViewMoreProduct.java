package app.kibbeh.model;

/**
 * Created by archi on 11/9/2016.
 */

public class ViewMoreProduct {

    private String id;
    private String name;
    private String owner;
    private String price;
    public String image;
    public String Qty;
    public String is_Fav;
    public String added_Cart;
    public String fav_Id;



    public String getMainCat() {
        return mainCat;
    }

    public String getFav_Id() {
        return fav_Id;
    }

    public void setFav_Id(String fav_Id) {
        this.fav_Id = fav_Id;
    }

    public void setMainCat(String mainCat) {
        this.mainCat = mainCat;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    private String mainCat;
    public int numOfSongs;

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getIs_Fav() {
        return is_Fav;
    }

    public void setIs_Fav(String is_Fav) {
        this.is_Fav = is_Fav;
    }

    public String getAdded_Cart() {
        return added_Cart;
    }

    public void setAdded_Cart(String added_Cart) {
        this.added_Cart = added_Cart;
    }

    public ViewMoreProduct() {

    }

    public ViewMoreProduct(String name, int numOfSongs) {
        this.name = name;
        this.numOfSongs = numOfSongs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }*/

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
