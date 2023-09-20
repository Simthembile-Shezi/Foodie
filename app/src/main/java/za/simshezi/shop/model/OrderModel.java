package za.simshezi.shop.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String shopId;
    private String customer;
    private String cellphone;
    private String payment;
    private String status;
    private Timestamp time;
    private Integer items;
    private Double price;

    public OrderModel() {
    }

    public OrderModel(String shopId, String customer, String cellphone, String payment, Timestamp time, Integer items, Double price) {
        this.shopId = shopId;
        this.customer = customer;
        this.cellphone = cellphone;
        this.payment = payment;
        this.time = time;
        this.items = items;
        this.price = price;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
