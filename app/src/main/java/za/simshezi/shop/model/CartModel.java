package za.simshezi.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.api.JavaAPI;

public class CartModel implements Serializable {
    private int DEST;
    private ShopModel shop;
    private Order order;
    private UserModel user;
    private String payment;
    private Double price;
    private ArrayList<ProductModel> list = new ArrayList<>();

    public CartModel(ShopModel shop) {
        this.shop = shop;
    }

    public CartModel(UserModel user) {
        this.user = user;
    }

    public ArrayList<ProductModel> getList() {
        return list;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }

    public void setList(ArrayList<ProductModel> list) {
        this.list = list;
    }

    public void add(ProductModel product) {
        list.add(product);
    }
    public void remove(ProductModel product){
        list.remove(product);
    }
    public double calculatePrice() {
        double total = 0;
        for(ProductModel model: list){
            total += model.getPrice();
        }
        return total;
    }
    public Double getPrice() {
        return price;
    }

    public ShopModel getShop() {
        return shop;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDEST() {
        return DEST;
    }

    public void setDEST(int DEST) {
        this.DEST = DEST;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = new Order(order);
    }

    public static class Order implements Serializable {
        private String id;
        private String shopId;
        private String customer;
        private String shopName;
        private String email;
        private String cellphone;
        private String payment;
        private String time;
        private String date;
        private Integer items;
        private Double price;
        private String status;

        public Order(OrderModel order) {
            this.id = order.getId();
            this.shopId = order.getShopId();
            this.customer = order.getCustomer();
            this.cellphone = order.getCellphone();
            this.email = order.getEmail();
            this.payment = order.getPayment();
            this.time = JavaAPI.getTime(order.getTime());
            this.date = JavaAPI.getDate(order.getTime());
            this.items = order.getItems();
            this.price = order.getPrice();
            this.status = order.getStatus();
            this.shopName = order.getShopName();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
