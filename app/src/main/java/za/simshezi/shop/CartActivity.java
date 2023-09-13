package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import za.simshezi.shop.adapter.CartAdapter;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ProductModel;

public class CartActivity extends AppCompatActivity {
    private RecyclerView lstProducts;
    private CartAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ProductModel> list;
    private TextView tvName, tvSubtotal, tvFees, tvTotal;
    private ImageView imgShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lstProducts = findViewById(R.id.lstCart);
        tvName = findViewById(R.id.tvCartShopName);
        tvSubtotal = findViewById(R.id.tvCartSubTotalAmount);
        tvFees = findViewById(R.id.tvCartFeesAmount);
        tvTotal = findViewById(R.id.tvCartTotalAmount);
        imgShop = findViewById(R.id.imgCartShopLogo);
        Intent intent = getIntent();
        CartModel cart = (CartModel) intent.getSerializableExtra("cart");
        if(cart != null) {
            float fees = cart.getPrice() * 0.1f;
            list = cart.getList();
            adapter = new CartAdapter(this,list, (view) -> {

            });
            layoutManager = new LinearLayoutManager(this);
            lstProducts.setAdapter(adapter);
            lstProducts.setLayoutManager(layoutManager);
            tvName.setText(cart.getShop());
            tvSubtotal.setText(String.format("R %s", cart.getPrice()));
            tvFees.setText(String.format("R %s", fees));
            tvTotal.setText(String.format("R %s", (cart.getPrice() + fees)));
            imgShop.setImageResource(R.drawable.icon);
        }else {
            finish();
        }
    }
}