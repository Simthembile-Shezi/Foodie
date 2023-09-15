package za.simshezi.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.CartAdapter;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ProductModel;

public class CartFragment extends Fragment {
    private RecyclerView lstProducts;
    private TextView tvName, tvSubtotal, tvFees, tvTotal;
    private ImageView imgShop;
    private SerializableModel model;
    private List<ProductModel> list;

    public void setModel(SerializableModel model) {
        this.model = model;
    }

    public CartFragment() {
        // Required empty public constructor
        model = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstProducts = view.findViewById(R.id.lstCart);
        tvName = view.findViewById(R.id.tvCartShopName);
        tvSubtotal = view.findViewById(R.id.tvCartSubTotalAmount);
        tvFees = view.findViewById(R.id.tvCartFeesAmount);
        tvTotal = view.findViewById(R.id.tvCartTotalAmount);
        imgShop = view.findViewById(R.id.imgCartShopLogo);
        build();
    }
    private void build(){
        if(model != null) {
            CartModel cart = (CartModel) model.getModel();
            float fees = cart.getPrice() * 0.05f;
            list = cart.getList();
            tvName.setText(cart.getShop());
            tvSubtotal.setText(String.format("R %s", cart.getPrice()));
            tvFees.setText(String.format("R %s", fees));
            tvTotal.setText(String.format("R %s", (cart.getPrice() + fees)));
            //imgShop.setImageBitmap(ImagesAPI.convertToBitmap(cart.getShop().getImage()));
            imgShop.setImageResource(R.drawable.image_1);
            CartAdapter adapter = new CartAdapter(getContext(), list, (view) -> {

            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            lstProducts.setAdapter(adapter);
            lstProducts.setLayoutManager(layoutManager);
        }else {
            tvName.setText("Cart is currently empty");
            imgShop.setImageResource(0);
        }
    }
}