package za.simshezi.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.style.IngredientItemDecoration;

public class AddProductCartActivity extends AppCompatActivity {
    private List<IngredientModel> ingredients;
    private IngredientAdapter adapter;
    private RecyclerView lstIngredients;
    private TextView tvName, tvDescription;
    private ImageView imgProduct;
    private TextView tvPrice;
    private ProductModel model;
    float total;
    private FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_cart);
        lstIngredients = findViewById(R.id.lstIngredients);
        tvName = findViewById(R.id.tvAddProductCartName);
        tvPrice = findViewById(R.id.tvAddProductCartPrice);
        tvDescription = findViewById(R.id.tvAddProductCartDescription);
        imgProduct = findViewById(R.id.imgAddProductCart);
        ingredients = new ArrayList<>();
        api = FirebaseAPI.getInstance();
        build();
    }
    private void build(){
        Intent intent = getIntent();
        model = (ProductModel) intent.getSerializableExtra("product");
        api.getIngredients(model.getProductId(), list -> {
            if(list != null){
                for(DocumentSnapshot documentSnapshot: list){
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String ingredientId = documentSnapshot.getId();
                        String productId = documentSnapshot.getString("productId");
                        String name = documentSnapshot.getString("name");
                        double price = documentSnapshot.getDouble("price");
                        ingredients.add(new IngredientModel(ingredientId, productId, name, (float) price));
                    }
                }
            }
        });
        adapter = new IngredientAdapter(ingredients, view -> {
            total = model.getPrice();
            for(IngredientModel model: IngredientAdapter.ingredients){
                if(model.getCount() > 0)
                    total += (model.getPrice() * model.getCount());
            }
            tvPrice.setText(String.format("R %s", total));
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration decoration = new IngredientItemDecoration();
        lstIngredients.setAdapter(adapter);
        lstIngredients.setLayoutManager(layoutManager);
        lstIngredients.addItemDecoration(decoration);
        if(model != null){
            tvName.setText(model.getName());
            tvDescription.setText(model.getDescription());
            tvPrice.setText(String.format("R %s", model.getPrice()));
            imgProduct.setImageResource(R.drawable.image_6);
            total = model.getPrice();
        }else {
            finish();
        }
    }

    public void addProductCartClicked(View view) {
        Intent data = getIntent();
        ProductModel model = (ProductModel) data.getSerializableExtra("product");
        if(model!=null) {
            List<IngredientModel> choices = new ArrayList<>();
            for(IngredientModel ingredient: IngredientAdapter.ingredients){
                if(ingredient.getCount() > 0){
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