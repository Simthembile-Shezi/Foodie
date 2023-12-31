package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCustomer, tvShop, tvPrice;
    private RadioButton btnCash, btnCard, btnWallet;
    private FirebaseAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvCustomer = findViewById(R.id.tvCheckoutCustomerName);
        tvShop = findViewById(R.id.tvCheckoutShopName);
        tvPrice = findViewById(R.id.tvChechoutPrice);
        btnCash = findViewById(R.id.btnCash);
        btnCard = findViewById(R.id.btnCard);
        btnWallet = findViewById(R.id.btnWallet);

        api = FirebaseAPI.getInstance();
        Intent intent = getIntent();
        CartModel cart = (CartModel) intent.getSerializableExtra("cart");
        if(cart != null){
            tvCustomer.setText(cart.getUser().getName());
            tvPrice.setText(String.format("R %s", cart.getPrice()));
            tvShop.setText(cart.getShop().getName());
        }
    }

    public void onCheckoutClicked(View view) {
        Intent intent = getIntent();
        CartModel cart = (CartModel) intent.getSerializableExtra("cart");
        if(cart != null){
            UserModel user = cart.getUser();
            String payment;
            if(btnCash.isChecked()){
                payment = btnCash.getText().toString();
            }else if(btnCard.isChecked() && user.isCard()){
                payment = btnCard.getText().toString();
            }else if(btnWallet.isChecked() && user.getCredit() > cart.getPrice()){
                payment = btnWallet.getText().toString();
                api.editAvailableBalance(user.getId(), (user.getCredit() - cart.getPrice()),
                        bool -> Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show());
            }else {
                Toast.makeText(this, "Select Valid Payment Method", Toast.LENGTH_SHORT).show();
                return;
            }
            cart.setPayment(payment);
            api.setOrder(cart, bool ->{
                if(bool){
                    Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show();
                    cart.setList(new ArrayList<>());
                    cart.setDEST(OrderFragment.ORDER_DEST);
                    Intent data = new Intent(CheckoutActivity.this, MainActivity.class);
                    data.putExtra("cart", cart);
                    startActivity(data);
                }else
                    Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            });
        }
    }
}