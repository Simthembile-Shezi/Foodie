package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.CartModel;
import za.simshezi.shop.model.UserModel;

public class CardPaymentsActivity extends AppCompatActivity {

    private EditText edCardHolder, edCardNumber, edExpiryDate, edCardCCV;
    private CartModel model;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payments);
        edExpiryDate = findViewById(R.id.edCardExpiryDate);
        edCardHolder = findViewById(R.id.edCardHolder);
        edCardNumber = findViewById(R.id.edCardNumber);
        edCardCCV = findViewById(R.id.edCardCCV);
        model = (CartModel) getIntent().getSerializableExtra("cart");
        if (model != null) {
            user = model.getUser();
        }else {
            finishAffinity();
        }
    }

    public void onAddClicked(View view) {
        String holder = edCardHolder.getText().toString().trim();
        String number = edCardNumber.getText().toString().trim();
        String date = edExpiryDate.getText().toString().trim();
        String ccv = edCardCCV.getText().toString().trim();

        if (TextUtils.isEmpty(holder)) {
            Toast.makeText(this, "Enter full names of card holder", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(number) && number.length() != 16) {
            Toast.makeText(this, "Enter a valid card number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(date) && date.length() != 6) {
            Toast.makeText(this, "Enter a valid expiry date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ccv) && ccv.length() != 3) {
            Toast.makeText(this, "Enter a valid ccv number", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAPI.getInstance().editCardStatus(user.getId(), bool -> runOnUiThread(() -> {
                if (bool) {
                    user.setCard(true);
                    Toast.makeText(this, "Card added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(this, "Failed adding card", Toast.LENGTH_SHORT).show();
                }
            }));
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