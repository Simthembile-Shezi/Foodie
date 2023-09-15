package za.simshezi.shop.mock;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;

public class ProductsData {
    public List<ProductModel> getData() {
        List<ProductModel> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(new ProductModel("productId","shopId", "Product "+i, "Short description",14.9f * i, new byte[0]));
        }
        return list;
    }
}
