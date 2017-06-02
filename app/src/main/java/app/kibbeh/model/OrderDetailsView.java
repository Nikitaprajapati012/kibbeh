package app.kibbeh.model;

/**
 * Created by Ravi Archi on 12/22/2016.
 */
public class OrderDetailsView {

    public String orderimage,orderprodetname,oerderquantity,orderprice;
    public OrderDetailsView(String orderImage, String order_prodectName, String oder_quantity, String order_price) {

        this.orderimage = orderImage;
        this.orderprodetname = order_prodectName;
        this.oerderquantity = oder_quantity;
        this.orderprice = order_price;
    }

    public String getOrderimage() {
        return orderimage;
    }

    public void setOrderimage(String orderimage) {
        this.orderimage = orderimage;
    }

    public String getOrderprodetname() {
        return orderprodetname;
    }

    public void setOrderprodetname(String orderprodetname) {
        this.orderprodetname = orderprodetname;
    }

    public String getOerderquantity() {
        return oerderquantity;
    }

    public void setOerderquantity(String oerderquantity) {
        this.oerderquantity = oerderquantity;
    }

    public String getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(String orderprice) {
        this.orderprice = orderprice;
    }
}
