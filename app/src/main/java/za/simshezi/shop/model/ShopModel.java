package za.simshezi.shop.model;

import java.io.Serializable;

public class ShopModel implements Serializable {
    private String shopId;
    private String name;
    private String email;
    private String cellphone;
    private float rating;
    private boolean status;
    private byte[] image;

    public ShopModel(String name) {
        this.name = name;
        this.status = false;
        this.rating = 0.0f;
    }

    public ShopModel(String shopId, String name, String email, String cellphone, float rating, boolean status, byte[] image) {
        this.shopId = shopId;
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
        this.rating = rating;
        this.status = status;
        this.image = image;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
