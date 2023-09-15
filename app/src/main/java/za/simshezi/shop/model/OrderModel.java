package za.simshezi.shop.model;

public class OrderModel {
    private String orderId;
    private String name;
    private String date;
    private int items;
    private float price;
    private String status;
    private byte[] image;

    public OrderModel(String orderId, String name, String date, int items, float price, String status, byte[] image) {
        this.orderId = orderId;
        this.name = name;
        this.date = date;
        this.items = items;
        this.price = price;
        this.status = status;
        this.image = image;
    }

    public String getShopId() {
        return orderId;
    }

    public void setShopId(String shopId) {
        this.orderId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
