package za.simshezi.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import za.simshezi.shop.adapter.ProductAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.style.ProductItemDecoration;

public class ShopProductActivity extends AppCompatActivity {
    public static final int REQ_CODE = 1999;
    private RecyclerView lstProducts;
    private ProductAdapter adapter;
    private TextView tvName;
    private ImageView imgShop;
    private Button btnCart;
    private CartModel cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product);
        lstProducts = findViewById(R.id.lstProducts);
        btnCart = findViewById(R.id.btnShopCart);
        tvName = findViewById(R.id.tvProductShopName);
        imgShop = findViewById(R.id.imgProductShopImage);
        build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ProductModel model = (ProductModel) data.getSerializableExtra("product");
            if (model != null) {
                if (btnCart.getVisibility() == View.GONE) {
                    btnCart.setVisibility(View.VISIBLE);
                }
                cart.add(model);
                btnCart.setText(String.format("R %.2f", cart.calculatePrice()));
            }
        }
    }

    private void build() {
        ArrayList<ProductModel> list = new ArrayList<>();
        FirebaseAPI api = FirebaseAPI.getInstance();
        Intent data = getIntent();
        cart = (CartModel) data.getSerializableExtra("cart");
        if (cart != null) {
            adapter = new ProductAdapter(model -> {
                ProductModel product = (ProductModel) model;
                Intent intent = new Intent(ShopProductActivity.this, AddProductCartActivity.class);
                intent.putExtra("product", product);
                startActivityForResult(intent, REQ_CODE);
            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            ProductItemDecoration decoration = new ProductItemDecoration();
            lstProducts.setAdapter(adapter);
            lstProducts.setLayoutManager(layoutManager);
            lstProducts.addItemDecoration(decoration);

            double price = cart.calculatePrice();
            if (price > 0) {
                btnCart.setText(String.format("R %.2f", price));
            } else {
                btnCart.setVisibility(View.GONE);
            }
            imgShop.setImageBitmap(ImagesAPI.convertToBitmap(cart.getShop().getImage()));
            tvName.setText(cart.getShop().getName());
            String id = cart.getShop().getId();
            api.getProducts(id, querySnapshot -> {
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot) {
                        ProductModel product = document.toObject(ProductModel.class);
                        if (product != null) {
                            product.setShopId(id);
                            product.setId(document.getId());
                            adapter.add(product);
                            new Thread(() -> api.getProductImage(document.getId(), bytes -> {
                                product.setImage(bytes);
                                list.add(product);
                                adapter.edit(product);
                            })).start();
                        }
                    }
                }
            });
        }
    }

    public void onCartClicked(View view) {
        Intent intent = new Intent(ShopProductActivity.this, MainActivity.class);
        cart.setDEST(CartFragment.CART_DEST);
        intent.putExtra("cart", cart);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopProductActivity.this, MainActivity.class);
        cart.setDEST(HomeFragment.HOME_DEST);
        intent.putExtra("cart", cart);
        startActivity(intent);
    }
}