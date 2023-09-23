package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences readPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String email = readPreferences.getString("email", "");
        String password = readPreferences.getString("password", "");
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SplashActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseAPI.getInstance().getCustomer(email, querySnapshot -> {
                                if (querySnapshot != null) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    UserModel user = document.toObject(UserModel.class);
                                    if (user != null) {
                                        user.setId(document.getId());
                                        CartModel model = new CartModel(user);
                                        model.setDEST(HomeFragment.HOME_DEST);
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        intent.putExtra("cart", model);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        startActivity(new Intent(this, LoginActivity.class));
                                    }
                                } else {
                                    startActivity(new Intent(this, LoginActivity.class));
                                }
                            });
                        }
                    });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}