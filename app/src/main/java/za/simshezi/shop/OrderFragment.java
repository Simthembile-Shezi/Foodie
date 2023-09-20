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
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.OrderAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.mock.OrdersData;
import za.simshezi.shop.model.OrderModel;
import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.UserModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class OrderFragment extends Fragment {
    private List<OrderModel> orders;
    private RecyclerView lstOrders;
    private TextView tvNoOrders;
    private SerializableModel model;


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
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
        build();
    }

    private void build() {
        if (model != null) {
            UserModel user = (UserModel) model.getModel();
            orders = new ArrayList<>();
            FirebaseAPI.getInstance().getOrders(user.getCellphone(), documentSnapshots -> {
                if(documentSnapshots != null && !documentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot document : documentSnapshots){
                        orders.add(document.toObject(OrderModel.class));
                    }
                    OrderAdapter adapter = new OrderAdapter(orders, (view) -> {

                    });
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    ShopItemDecoration decoration = new ShopItemDecoration();
                    lstOrders.setAdapter(adapter);
                    lstOrders.setLayoutManager(layoutManager);
                    lstOrders.addItemDecoration(decoration);

                    lstOrders.setVisibility(View.VISIBLE);
                    tvNoOrders.setVisibility(View.GONE);
                }else {
                    lstOrders.setVisibility(View.GONE);
                    tvNoOrders.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void setModel(SerializableModel model) {
        this.model = model;
    }
}