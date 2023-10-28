package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import za.simshezi.shop.adapter.OrderProductAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;

public class ManageOrderActivity extends AppCompatActivity {
    private FirebaseAPI api;
    private CartModel cart;
    private RecyclerView lstProduct;
    private TextView tvName, tvShopName, tvPayment, tvETA, tvStatus, tvPrice;
    private ArrayList<ProductModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        lstProduct = findViewById(R.id.lstOrderProducts);
        tvName = findViewById(R.id.tvOrderSummeryCustomerName);
        tvShopName = findViewById(R.id.tvOrderSummeryShopName);
        tvETA = findViewById(R.id.tvOrderSummeryTime);
        tvStatus = findViewById(R.id.tvOrderSummeryStatus);
        tvPayment = findViewById(R.id.tvOrderSummeryPayment);
        tvPrice = findViewById(R.id.tvOrderSummeryPrice);
        cart = (CartModel) getIntent().getSerializableExtra("cart");
        api = FirebaseAPI.getInstance();
        build();
    }

    private void build() {
        String orderId = cart.getOrder().getId();
        tvName.setText(cart.getOrder().getCustomer());
        tvShopName.setText(cart.getOrder().getShopName());
        tvPayment.setText(cart.getOrder().getPayment());
        tvETA.setText(cart.getOrder().getTime());
        tvStatus.setText(cart.getOrder().getStatus());
        tvPrice.setText(String.format("R %.2f", cart.getOrder().getPrice()));
        list = new ArrayList<>();
        api.getOrderProducts(orderId, queryDocument -> {
            if(queryDocument != null){
                for(DocumentSnapshot document: queryDocument){
                    ProductModel product = document.toObject(ProductModel.class);
                    if(product != null) {
                        product.setId(document.getId());
                        product.setIngredients(new ArrayList<>());
                        api.getOrderIngredients(orderId, product.getId(), queryDocs -> {
                            if(queryDocs != null){
                                for (DocumentSnapshot doc : queryDocs) {
                                    IngredientModel ingredient = doc.toObject(IngredientModel.class);
                                    if(ingredient != null){
                                        ingredient.setId(doc.getId());
                                        product.addIngredients(ingredient);
                                    }
                                }
                            }
                            list.add(product);
                            if(list.size() == queryDocument.size()){
                                OrderProductAdapter adapter = new OrderProductAdapter(this, list);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                                lstProduct.setAdapter(adapter);
                                lstProduct.setLayoutManager(layoutManager);
                            }
                        });
                    }

                }

            }
        });

    }

    public void onOrderExit(View view) {
        finish();
    }
}