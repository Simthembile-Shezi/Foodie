package za.simshezi.shop.model;

import java.io.Serializable;

public class IngredientModel implements Serializable {
    private String id;    //PK
    private String productId;       //FK
    private String name;
    private Double price;
    private Integer count;

    public IngredientModel() {
    }

    public IngredientModel(String productId, String name, Double price) {
        this.id = productId + name.replace(" ", "0");
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void resetId(String productId){
        this.id = productId + name.replace(" ", "0");
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}