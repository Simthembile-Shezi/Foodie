package za.simshezi.shop;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.OrderAdapter;
import za.simshezi.shop.mock.OrdersData;
import za.simshezi.shop.model.OrderModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class OrderFragment extends Fragment {
    private List<OrderModel> orders;
    private RecyclerView lstOrders;
    public OrderFragment() {
        orders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstOrders = view.findViewById(R.id.lstOrders);
        orders = new OrdersData().getData();
        OrderAdapter adapter = new OrderAdapter(orders, (v) ->{

        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ShopItemDecoration decoration = new ShopItemDecoration();
        lstOrders.setAdapter(adapter);
        lstOrders.setLayoutManager(layoutManager);
        lstOrders.addItemDecoration(decoration);
    }
}