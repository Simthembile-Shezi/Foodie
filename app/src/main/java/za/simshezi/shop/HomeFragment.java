package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import za.simshezi.shop.adapter.ShopAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static int HOME_DEST = 0;
    private FloatingActionButton btnRefresh;
    private SearchView searchView;
    private RecyclerView lstShops;
    private List<ShopModel> shops;
    private ShopAdapter adapter;
    private FirebaseAPI api;

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
        btnRefresh = view.findViewById(R.id.btnHomeRefresh);
        build();
    }

    private void build() {
        CartModel cart = (CartModel) requireActivity().getIntent().getSerializableExtra("cart");
        api = FirebaseAPI.getInstance();
        shops = new ArrayList<>();
        if (cart != null) {
            update(cart);
            api.getShops((DocumentSnapshot -> {
                if (DocumentSnapshot != null) {
                    for (QueryDocumentSnapshot document : DocumentSnapshot) {
                        ShopModel shop = document.toObject(ShopModel.class);
                        shop.setId(document.getId());
                        adapter.add(shop);
                        new Thread(() -> api.getShopImage(shop.getId(), bytes -> {
                            shop.setImage(bytes);
                            shops.add(shop);
                            adapter.edit(shop);
                        })).start();
                    }
                }
            }));
        }
    }

    private void update(CartModel cart) {
        adapter = new ShopAdapter(model -> {
            ShopModel shopModel = (ShopModel) model;
            Intent intent = new Intent(getContext(), ShopProductActivity.class);
            if (cart.getShop() == null) {
                cart.setShop(shopModel);
            } else if (!Objects.equals(cart.getShop().getName(), shopModel.getName())) {
                if (cart.calculatePrice() > 0.0) {
                    cart.setList(new ArrayList<>());
                    Toast.makeText(getContext(), String.format("Incomplete order from %s has been removed", cart.getShop().getName()), Toast.LENGTH_LONG).show();
                }
                cart.setShop(shopModel);
            } else {
                cart.getShop().setImage(shopModel.getImage());
            }
            intent.putExtra("cart", cart);
            startActivity(intent);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ShopItemDecoration decoration = new ShopItemDecoration();
        lstShops.setAdapter(adapter);
        lstShops.setLayoutManager(layoutManager);
        lstShops.addItemDecoration(decoration);
        btnRefresh.setOnClickListener(view -> build());
        searchView.setOnClickListener(view -> searchView.setIconified(false));
        searchView.setOnQueryTextListener(HomeFragment.this);
    }

    private boolean filter(String text) {
        // creating a new array list to filter shops.
        ArrayList<ShopModel> filtered = new ArrayList<>();

        for (ShopModel item : shops) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (filtered.isEmpty()) {
            Toast.makeText(getContext(), "No Shop Found..", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            adapter.filter(filtered);
            return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return filter(s);
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}