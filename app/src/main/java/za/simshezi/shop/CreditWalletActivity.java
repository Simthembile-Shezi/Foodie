package za.simshezi.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.Objects;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;

public class CreditWalletActivity extends AppCompatActivity {
    private EditText edVoucher;
    private TextView tvAvailableCredit;
    private FirebaseAPI api;
    private UserModel user;
    private CartModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_wallet);
        api = FirebaseAPI.getInstance();
        edVoucher = findViewById(R.id.edEnterCredit);
        tvAvailableCredit = findViewById(R.id.tvAvailableCredit);
        model = (CartModel) getIntent().getSerializableExtra("cart");
        if (model != null) {
            user = model.getUser();
            tvAvailableCredit.setText(String.format("R %.2f", user.getCredit()));
        }
    }

    public void onRefundClicked(View view) {
    }

    public void addVoucherClicked(View view) {
        String code = edVoucher.getText().toString().trim();
        if (code.length() != 12) {
            Toast.makeText(this, "Enter voucher code", Toast.LENGTH_SHORT).show();
        } else {
            api.getVoucher(code, documentSnapshot -> {
                Double value = documentSnapshot.getDouble("amount");
                Timestamp expiry = documentSnapshot.getTimestamp("expiry");
                if (value != null && expiry != null) {
                    if (expiry.compareTo(Timestamp.now()) > 0) {
                        user.setCredit(user.getCredit() + value);
                        new Thread(() -> api.editAvailableBalance(user.getId(), user.getCredit(), bool -> runOnUiThread(() -> {
                            if (bool) {
                                Toast.makeText(this, "Voucher added", Toast.LENGTH_SHORT).show();
                                tvAvailableCredit.setText(String.format("R %.2f", user.getCredit()));
                                edVoucher.setText("");
                                api.deleteVoucher(code);
                            } else {
                                Toast.makeText(this, "Voucher failed, try again", Toast.LENGTH_SHORT).show();
                            }
                        }))).start();
                    } else {
                        Toast.makeText(this, "Expired voucher", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid voucher", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        model.setUser(user);
        model.setDEST(ProfileFragment.PROFILE_DEST);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cart", model);
        startActivity(intent);
    }
}