package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import za.simshezi.shop.adapter.ShopAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FloatingActionButton btnFilter;
    private SearchView searchView;
    private RecyclerView lstShops;
    private List<ShopModel> shops;
    private ShopAdapter adapter;
    private FirebaseAPI api;
    private SerializableModel model;

    public void setModel(SerializableModel model) {
        this.model = model;
    }

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstShops = view.findViewById(R.id.lstShops);
        searchView = view.findViewById(R.id.searchViewHome);
        btnFilter = view.findViewById(R.id.btnHomeFilter);
        build();
    }

    private void build() {
        shops = new ArrayList<>();
        api = FirebaseAPI.getInstance();
        api.getShops((DocumentSnapshot -> {
            if (DocumentSnapshot != null) {
                for (QueryDocumentSnapshot document : DocumentSnapshot) {
                    ShopModel shop = document.toObject(ShopModel.class);
                    shop.setId(document.getId());
                    api.getShopImage(shop.getId(), bytes -> {
                        if (bytes != null) {
                            shop.setImage(bytes);
                        }
                        shops.add(shop);
                        if (DocumentSnapshot.size() == shops.size()) {
                            adapter = new ShopAdapter(shops, view -> {
                                Intent intent = new Intent(getContext(), ShopProductActivity.class);
                                ShopModel shopModel = adapter.shop;
                                CartModel cart = (CartModel) model.getModel();
                                if(cart.getShop() == null){
                                    cart.setShop(shopModel);
                                }else if (!Objects.equals(cart.getShop().getName(), shopModel.getName())) {
                                    cart.setShop(shopModel);
                                    Toast.makeText(getContext(), String.format("Incomplete order from %s will be removed", cart.getShop()), Toast.LENGTH_LONG).show();
                                }
                                intent.putExtra("cart", cart);
                                startActivity(intent);
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            ShopItemDecoration decoration = new ShopItemDecoration();
                            lstShops.setAdapter(adapter);
                            lstShops.setLayoutManager(layoutManager);
                            lstShops.addItemDecoration(decoration);

                            searchView.setOnQueryTextListener(HomeFragment.this);
                        }
                    });
                }
            }
        }));
    }

    private void filter(String text) {
        // creating a new array list to filter shops.
        ArrayList<ShopModel> filtered = new ArrayList<>();

        for (ShopModel item : shops) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (filtered.isEmpty()) {
            Toast.makeText(getContext(), "No Shop Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filtered);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filter(s);
        return false;
    }
}