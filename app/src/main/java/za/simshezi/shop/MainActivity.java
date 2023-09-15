package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import za.simshezi.shop.model.SerializableModel;
import za.simshezi.shop.model.CartModel;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private CartFragment cartFragment = new CartFragment();
    private OrderFragment orderFragment = new OrderFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private CartModel cartModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_dest);
        Intent intent = getIntent();
        CartModel cart = (CartModel) intent.getSerializableExtra("cart");
        if (cart != null) {
            if (cartModel != null) {
                if(cart.getShop().equals(cartModel.getShop()))
                    cartModel.getList().addAll(cart.getList());
            } else {
                cartModel = cart;
            }
            cartFragment.setModel(() -> cartModel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
}