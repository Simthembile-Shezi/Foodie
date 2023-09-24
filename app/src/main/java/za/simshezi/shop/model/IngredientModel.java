package za.simshezi.shop.model;

import android.widget.TextView;

import java.io.Serializable;

public class IngredientModel implements Serializable {
    private String ingredientId;    //PK
    private String productId;       //FK
    private String name;
    private Double price;
    private Integer count;

    public IngredientModel() {
    }

    public IngredientModel(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
}
