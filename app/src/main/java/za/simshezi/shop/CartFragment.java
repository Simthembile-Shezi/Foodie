package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.CartAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ProductModel;

public class CartFragment extends Fragment {
    private RecyclerView lstProducts;
    private TextView tvName, tvSubtotal, tvFees, tvTotal, tvOrderSummery;
    private ConstraintLayout constraintEmptyCart, constraintCart;
    private Button btnCheckout;
    private ImageView imgShop;
    private SerializableModel model;
    private List<ProductModel> list;
    private FirebaseAPI api;

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
        tvOrderSummery = view.findViewById(R.id.tvOrderSummery);
        tvSubtotal = view.findViewById(R.id.tvCartSubTotalAmount);
        tvFees = view.findViewById(R.id.tvCartFeesAmount);
        tvTotal = view.findViewById(R.id.tvCartTotalAmount);
        imgShop = view.findViewById(R.id.imgCartShopLogo);
        constraintEmptyCart = view.findViewById(R.id.layoutEmptyCart);
        constraintCart = view.findViewById(R.id.layoutCart);
        btnCheckout = view.findViewById(R.id.btnCheckOut);
        build();
    }
    private void build(){
        if(model != null) {
            api = FirebaseAPI.getInstance();
            constraintEmptyCart.setVisibility(View.GONE);
            constraintCart.setVisibility(View.VISIBLE);
            CartModel cart = (CartModel) model.getModel();
            float fees = cart.getPrice() * 0.05f;
            list = cart.getList();
            tvName.setText(cart.getShop().getName());
            tvSubtotal.setText(String.format("R %s", cart.getPrice()));
            tvFees.setText(String.format("R %s", fees));
            tvTotal.setText(String.format("R %s", (cart.getPrice() + fees)));
            byte[] image = cart.getShop().getImage();
            if(image != null){
                imgShop.setImageBitmap(ImagesAPI.convertToBitmap(image));
            }else
                imgShop.setImageResource(R.drawable.baseline_fastfood_24);
            CartAdapter adapter = new CartAdapter(getContext(), list, (view) -> {

            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            lstProducts.setAdapter(adapter);
            lstProducts.setLayoutManager(layoutManager);
            btnCheckout.setOnClickListener(view -> {
                cart.setPayment("Cash");
                api.setOrder(cart, bool ->{
                    if(bool){
                        Toast.makeText(getContext(), "Order placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                    }else
                        Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                });
            });
        }else {
            constraintCart.setVisibility(View.GONE);
            constraintEmptyCart.setVisibility(View.VISIBLE);
        }
    }
}