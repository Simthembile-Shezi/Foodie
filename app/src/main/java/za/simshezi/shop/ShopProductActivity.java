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
        api = FirebaseAPI.getInstance();
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
        api.getProducts(shop.getShopId(), list ->{
            if(list != null){
                for(DocumentSnapshot documentSnapshot: list){
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String productId = documentSnapshot.getId();
                        String shopId = documentSnapshot.getString("shopId");
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        double price = documentSnapshot.getDouble("price");
                        api.getProductImage(productId, bytes -> {
                            if (bytes != null) {
                                ProductModel model = new ProductModel(productId, shopId, name, description, (float) price, bytes);
                                products.add(model);
                            }
                        });
                    }
                }
            }
        });
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
        imgShop.setImageResource(R.drawable.icon);
        tvName.setText(shop.getName());
        //imgShop.setImageBitmap(ImagesAPI.convertToBitmap(shop.getImage()));
    }
    public void onCartClicked(View view){
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("cart", cart);
        startActivity(intent);
    }
}