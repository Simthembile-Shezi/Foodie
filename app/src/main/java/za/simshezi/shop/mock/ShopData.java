package za.simshezi.shop.mock;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.ShopModel;

public class ShopData {
    public List<ShopModel> getData() {
        List<ShopModel> list = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            //list.add(new ShopModel("shopId", "Shop " + i, "shop@email.org", "0785469321", 0.5 * i, "Open", new byte[0]));
        }
        return list;
    }
}
