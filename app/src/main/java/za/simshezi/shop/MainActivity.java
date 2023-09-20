package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private int finish = 0;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private CartFragment cartFragment = new CartFragment();
    private OrderFragment orderFragment = new OrderFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        CartModel cart = (CartModel) intent.getSerializableExtra("cart");
        if (cart != null) {
            cartFragment.setModel(() -> cart);
            homeFragment.setModel(() -> cart);
            orderFragment.setModel(cart::getUser);
            profileFragment.setModel(cart::getUser);

            bottomNavigationView.setSelectedItemId(R.id.cart_dest);
        }else {
            UserModel user = (UserModel) intent.getSerializableExtra("user");
            CartModel model = new CartModel(user);
            cartFragment.setModel(() -> model);
            homeFragment.setModel(() -> model);
            orderFragment.setModel(() -> user);
            profileFragment.setModel(() -> user);
            bottomNavigationView.setSelectedItemId(R.id.home_dest);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        finish = 0;
        int itemId = item.getItemId();
        if (itemId == R.id.home_dest) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        }else if (itemId == R.id.cart_dest) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, cartFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.order_dest) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, orderFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.profile_dest) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish++;
        if(finish == 2) {
            finishAffinity();
        }else {
            Toast.makeText(this, "Press back again to exit the app", Toast.LENGTH_SHORT).show();
        }
    }
}