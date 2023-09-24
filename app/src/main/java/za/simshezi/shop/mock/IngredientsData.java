package za.simshezi.shop.mock;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ShopModel;

public class IngredientsData {
    public List<IngredientModel> getData() {
        List<IngredientModel> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            //list.add(new IngredientModel("ingredientId", "productId", "Ingredient "+i, 14.9f * i));
        }
        return list;
    }
}
