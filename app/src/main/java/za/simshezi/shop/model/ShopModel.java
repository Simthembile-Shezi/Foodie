package za.simshezi.shop.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopModel implements Serializable {
    private String id;
    private String name;
    private String email;
    private String cellphone;
    private Double rating;
    private String status;
    private String address;
    private byte[] image;
    private ArrayList<ProductModel> products = new ArrayList<>();

    public ShopModel() {
    }

    public ShopModel(String name, String email, String cellphone, Double rating, String status, String address, byte[] image) {
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
        this.rating = rating;
        this.status = status;
        this.address = address;
        this.image = image;
    }

    public ShopModel(String name, String cellphone, String address, byte[] image) {
        this.name = name;
        this.cellphone = cellphone;
        this.address = address;
        this.image = image;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ArrayList<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductModel> products) {
        this.products = products;
    }
}
