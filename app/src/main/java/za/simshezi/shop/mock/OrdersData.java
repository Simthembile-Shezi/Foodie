package za.simshezi.shop.mock;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.model.OrderModel;

public class OrdersData {
    public List<OrderModel> getData() {
        List<OrderModel> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(new OrderModel("shopId","Name "+i,"0789632023", "Cash", Timestamp.now(), 10*i, 9.99*i));
        }
        return list;
    }
}
