package za.simshezi.shop.model;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements Serializable {
    private int DEST;
    private ShopModel shop;
    private UserModel user;
    private String payment;
    private Double price;
    private List<ProductModel> list;
    private List<ShopModel> shops = new ArrayList<>();

    public CartModel(ShopModel shop) {
        this.shop = shop;
        this.list = new ArrayList<>();
    }

    public CartModel(UserModel user) {
        this.user = user;
        this.list = new ArrayList<>();
    }

    public List<ProductModel> getList() {
        return list;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }

    public void setList(List<ProductModel> list) {
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

    public List<ShopModel> getShops() {
        return shops;
    }

    public void setShops(List<ShopModel> shops) {
        this.shops = shops;
    }
}
