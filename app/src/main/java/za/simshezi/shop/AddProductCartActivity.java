package za.simshezi.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import za.simshezi.shop.adapter.IngredientAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.mock.IngredientsData;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.style.IngredientItemDecoration;

public class AddProductCartActivity extends AppCompatActivity {
    private List<IngredientModel> ingredients;
    private IngredientAdapter adapter;
    private RecyclerView lstIngredients;
    private TextView tvName, tvDescription;
    private ImageView imgProduct;
    private TextView tvPrice, tvNormal;
    private Button btnAddCart;
    private ProductModel model;
    Double total;
    private FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_cart);
        lstIngredients = findViewById(R.id.lstIngredients);
        tvName = findViewById(R.id.tvAddProductCartName);
        tvPrice = findViewById(R.id.tvAddProductCartPrice);
        tvDescription = findViewById(R.id.tvAddProductCartDescription);
        tvNormal = findViewById(R.id.tvNormalServing);
        imgProduct = findViewById(R.id.imgAddProductCart);
        btnAddCart = findViewById(R.id.btnAddProductCart);
        build();
    }

    private void build() {
        Intent intent = getIntent();
        model = (ProductModel) intent.getSerializableExtra("product");
        if(model != null){
            tvName.setText(model.getName());
            tvDescription.setText(model.getDescription());
            tvPrice.setText(String.format("R %s", JavaAPI.formatDouble(model.getPrice())));
            btnAddCart.setText(String.format("Add : R %s", JavaAPI.formatDouble(model.getPrice())));
            if(model.getImage() != null) {
                imgProduct.setImageBitmap(ImagesAPI.convertToBitmap(model.getImage()));
            }else {
                imgProduct.setImageResource(R.drawable.baseline_fastfood_24);
            }
            total = model.getPrice();
            ingredients = new ArrayList<>();
            api = FirebaseAPI.getInstance();
            api.getIngredients(model.getShopId(), model.getId(), queryDocumentSnapshots -> {
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot document: queryDocumentSnapshots){
                        IngredientModel ingredient = document.toObject(IngredientModel.class);
                        if(ingredient != null){
                            ingredient.setIngredientId(document.getId());
                            ingredient.setCount(0);
                            ingredients.add(ingredient);
                        }
                        if(ingredients.size() == queryDocumentSnapshots.size()){
                           update();
                        }
                    }
                }else {
                    tvNormal.setVisibility(View.VISIBLE);
                    lstIngredients.setVisibility(View.GONE);
                    update();
                }
            });
        }
    }

    private void update() {
        adapter = new IngredientAdapter(ingredients, view -> {
            total = model.getPrice();
            for (IngredientModel model : IngredientAdapter.ingredients) {
                if (model.getCount() > 0)
                    total += (model.getPrice() * model.getCount());
            }
            btnAddCart.setText(String.format("Add : R %s", JavaAPI.formatDouble(total)));
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration decoration = new IngredientItemDecoration();
        lstIngredients.setAdapter(adapter);
        lstIngredients.setLayoutManager(layoutManager);
        lstIngredients.addItemDecoration(decoration);
    }

    public void addProductCartClicked(View view) {
        Intent data = getIntent();
        ProductModel model = (ProductModel) data.getSerializableExtra("product");
        if (model != null) {
            List<IngredientModel> choices = new ArrayList<>();
            for (IngredientModel ingredient : IngredientAdapter.ingredients) {
                if (ingredient.getCount() > 0) {
                    choices.add(ingredient);
                }
            }
            model.setIngredients(choices);
            model.setPrice(total);
            Intent intent = new Intent();
            intent.putExtra("product", model);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}