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
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.OrderModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class OrderFragment extends Fragment {
    public static int ORDER_DEST = 2;
    private List<OrderModel> orders;
    private RecyclerView lstOrders;
    private TextView tvNoOrders;

    public OrderFragment() {
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
        CartModel model = (CartModel) requireActivity().getIntent().getSerializableExtra("cart");
        if (model != null) {
            orders = new ArrayList<>();
            FirebaseAPI.getInstance().getOrders(model.getUser().getEmail(), documentSnapshots -> {
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
}