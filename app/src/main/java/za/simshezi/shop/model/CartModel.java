package za.simshezi.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements Serializable {
    private final ShopModel shop;
    private List<ProductModel> list;

    public CartModel(ShopModel shop) {
        this.shop = shop;
        this.list = new ArrayList<>();
    }

    public List<ProductModel> getList() {
        return list;
    }

    public void add(ProductModel product) {
        list.add(product);
    }
    public void remove(ProductModel product){
        list.remove(product);
    }
    public float getPrice() {
        float total = 0;
        for(ProductModel model: list){
            total += model.getPrice();
        }
        return total;
    }

    public String getShop() {
        return shop.getName();
    }
}
