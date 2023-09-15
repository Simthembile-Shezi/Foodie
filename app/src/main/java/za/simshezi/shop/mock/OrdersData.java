package za.simshezi.shop.mock;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.OrderModel;

public class OrdersData {
    public List<OrderModel> getData() {
        List<OrderModel> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(new OrderModel("orderId","Shop Name", "02/06/2023", 3,14.9f, true, new byte[0]));
        }
        return list;
    }
}
