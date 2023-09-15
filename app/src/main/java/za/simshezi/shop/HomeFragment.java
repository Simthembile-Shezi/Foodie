package za.simshezi.shop;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.ShopAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.mock.ShopData;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.style.ShopItemDecoration;

public class HomeFragment extends Fragment {
    private FloatingActionButton btnFilter;
    private SearchView searchView;
    private RecyclerView lstShops;
    private List<ShopModel> shops;
    private ShopAdapter adapter;
    private FirebaseAPI api;

    public HomeFragment() {
        //api = FirebaseAPI.getInstance();
        //shops = api.getShops();

        shops = new ShopData().getData();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void build() {
        adapter = new ShopAdapter(shops, view -> {
            Intent intent = new Intent(getContext(), ShopProductActivity.class);
            intent.putExtra("shop", adapter.shop);
            startActivity(intent);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ShopItemDecoration decoration = new ShopItemDecoration();
        lstShops.setAdapter(adapter);
        lstShops.setLayoutManager(layoutManager);
        lstShops.addItemDecoration(decoration);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
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
}