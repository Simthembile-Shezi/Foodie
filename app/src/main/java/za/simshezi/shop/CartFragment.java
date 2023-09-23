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

import java.util.List;

import za.simshezi.shop.adapter.CartAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ProductModel;

public class CartFragment extends Fragment {
    public static int CART_DEST = 1;
    private RecyclerView lstProducts;
    private TextView tvName, tvSubtotal, tvFees, tvTotal;
    private ConstraintLayout constraintEmptyCart, constraintCart;
    private Button btnCheckout;
    private ImageView imgShop;
    private List<ProductModel> list;
    private FirebaseAPI api;

    public CartFragment() {
        // Required empty public constructor
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
        constraintEmptyCart = view.findViewById(R.id.layoutEmptyCart);
        constraintCart = view.findViewById(R.id.layoutCart);
        btnCheckout = view.findViewById(R.id.btnCheckOut);
        build();
    }

    private void build() {
        CartModel cart = (CartModel) requireActivity().getIntent().getSerializableExtra("cart");
        if (cart != null) {
            api = FirebaseAPI.getInstance();
            if (cart.getList().isEmpty()) {
                constraintCart.setVisibility(View.GONE);
                constraintEmptyCart.setVisibility(View.VISIBLE);
                return;
            }
            double subtotal = cart.calculatePrice();
            double fees = subtotal * 0.05;
            double total = subtotal + fees;
            list = cart.getList();
            tvName.setText(cart.getShop().getName());
            tvSubtotal.setText(String.format("R %s", subtotal));
            tvFees.setText(String.format("R %s", fees));
            tvTotal.setText(String.format("R %s", total));
            byte[] image = cart.getShop().getImage();
            if (image != null) {
                imgShop.setImageBitmap(ImagesAPI.convertToBitmap(image));
            } else
                imgShop.setImageResource(R.drawable.baseline_fastfood_24);
            CartAdapter adapter = new CartAdapter(getContext(), list, (view) -> {

            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            lstProducts.setAdapter(adapter);
            lstProducts.setLayoutManager(layoutManager);
            btnCheckout.setOnClickListener(view -> {
                cart.setPrice(total);
                Intent intent = new Intent(requireContext(), CheckoutActivity.class);
                intent.putExtra("cart", cart);
                startActivity(intent);
            });
            constraintEmptyCart.setVisibility(View.GONE);
            constraintCart.setVisibility(View.VISIBLE);
        } else {
            constraintCart.setVisibility(View.GONE);
            constraintEmptyCart.setVisibility(View.VISIBLE);
        }
    }
}