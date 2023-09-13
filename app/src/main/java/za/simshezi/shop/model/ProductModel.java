package za.simshezi.shop.model;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private String productId;          //PK
    private String shopId;      //FK
    private String name;
    private String description;
    private float price;
    private byte[] image;
    private List<IngredientModel> ingredients;

    public ProductModel(String productId, String shopId, String name, String description, float price, byte[] image) {
        this.productId = productId;
        this.shopId = shopId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }
}
