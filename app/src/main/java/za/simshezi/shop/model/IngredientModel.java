package za.simshezi.shop.model;

import android.widget.TextView;

import java.io.Serializable;

public class IngredientModel implements Serializable {
    private String ingredientId;    //PK
    private String productId;       //FK
    private String name;
    private float price;
    private int count;

    public IngredientModel(String ingredientId, String productId, String name, float price) {
        this.ingredientId = ingredientId;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
