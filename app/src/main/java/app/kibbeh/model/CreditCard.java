package app.kibbeh.model;

/**
 * Created by archi on 11/15/2016.
 */

public class CreditCard
{
    String id,custmoreId,cardToken,lastFour,craete,updated,userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustmoreId() {
        return custmoreId;
    }

    public void setCustmoreId(String custmoreId) {
        this.custmoreId = custmoreId;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public String getCraete() {
        return craete;
    }

    public void setCraete(String craete) {
        this.craete = craete;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
