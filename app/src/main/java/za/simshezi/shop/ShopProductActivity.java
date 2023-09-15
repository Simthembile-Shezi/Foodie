package za.simshezi.shop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.ProductAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.mock.ProductsData;
import za.simshezi.shop.mock.ShopData;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ShopModel;
import za.simshezi.shop.style.ProductItemDecoration;

public class ShopProductActivity extends AppCompatActivity {
    public static final int REQ_CODE = 1999;
    private RecyclerView lstProducts;
    private List<ProductModel> products;
    private ProductAdapter adapter;
    private TextView tvPrice, tvName;
    private ImageView imgShop;
    private CartModel cart;
    private FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product);
        lstProducts = findViewById(R.id.lstProducts);
        tvPrice = findViewById(R.id.tvCartPrice);
        tvName = findViewById(R.id.tvProductShopName);
        imgShop = findViewById(R.id.imgProductShopImage);
        products = new ArrayList<>();
        //api = FirebaseAPI.getInstance();
        build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data != null){
            ProductModel model = (ProductModel) data.getSerializableExtra("product");
            if(model != null){
                cart.add(model);
                tvPrice.setText(String.format("R %s", cart.getPrice()));
            }
        }
    }

    private void build(){
        Intent data = getIntent();
        ShopModel shop = (ShopModel) data.getSerializableExtra("shop");
        //products = api.getProducts(shop.getShopId());
        products = new ProductsData().getData();
        cart = new CartModel(shop);
        adapter = new ProductAdapter(products, view -> {
            Intent intent = new Intent(this, AddProductCartActivity.class);
            intent.putExtra("product", adapter.product);
            startActivityForResult(intent, REQ_CODE);
        });
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        ProductItemDecoration decoration = new ProductItemDecoration();
        lstProducts.setAdapter(adapter);
        lstProducts.setLayoutManager(layoutManager);
        lstProducts.addItemDecoration(decoration);
        imgShop.setImageBitmap(ImagesAPI.convertToBitmap(shop.getImage()));
        tvName.setText(shop.getName());
    }
    public void onCartClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cart", cart);
        startActivity(intent);
    }
}