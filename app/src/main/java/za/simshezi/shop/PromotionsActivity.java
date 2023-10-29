package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentSnapshot;

import za.simshezi.shop.adapter.PromotionAdapter;
import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.PromotionModel;

public class PromotionsActivity extends AppCompatActivity {
    private FirebaseAPI api;
    private PromotionAdapter adapter;
    private RecyclerView lstPromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        lstPromotions = findViewById(R.id.lstPromotions);
        api = FirebaseAPI.getInstance();
        build();
    }

    private void build() {
        adapter = new PromotionAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        lstPromotions.setAdapter(adapter);
        lstPromotions.setLayoutManager(layoutManager);
        api = FirebaseAPI.getInstance();
        api.getPromotions(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    PromotionModel promotion = document.toObject(PromotionModel.class);
                    if (promotion != null) {
                        promotion.setPromoCode(document.getId());
                        adapter.add(promotion);
                    }
                }
            }
        });
    }
}