package za.simshezi.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.ShopAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class HomeFragment extends Fragment {
    private RecyclerView lstShops;
    private List<ShopModel> shops;
    private ShopAdapter adapter;
    private FirebaseAPI api;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lstShops = view.findViewById(R.id.lstShops);
        build();
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void build() {
        shops = new ArrayList<>();
        api = FirebaseAPI.getInstance();
        api.getShops( list ->{
            if(list != null){
                for(DocumentSnapshot documentSnapshot: list){
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        String cellphone = documentSnapshot.getString("cellphone");
                        String email = documentSnapshot.getString("email");
                        boolean status = documentSnapshot.getBoolean("status");
                        double rating = documentSnapshot.getDouble("rating");
                        api.getShopImage(id, bytes -> {
                            if (bytes != null) {
                                ShopModel model = new ShopModel(id ,name, email, cellphone, (float) rating, status, bytes);
                                shops.add(model);
                            }
                        });
                    }
                }
            }
        });
        adapter = new ShopAdapter(shops, view -> {
            Intent intent = new Intent(context , ShopProductActivity.class);
            intent.putExtra("shop", adapter.shop);
            startActivity(intent);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ShopItemDecoration decoration = new ShopItemDecoration();
        lstShops.setAdapter(adapter);
        lstShops.setLayoutManager(layoutManager);
        lstShops.addItemDecoration(decoration);
    }
}