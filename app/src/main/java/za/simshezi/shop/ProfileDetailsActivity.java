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

public class ProfileDetailsActivity extends AppCompatActivity {

    private EditText edFullName, edCellphone;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        edFullName = findViewById(R.id.edProfileDetailsFullname);
        edCellphone = findViewById(R.id.edProfileDetailsCellphone);

        Intent intent = getIntent();
        CartModel model = (CartModel) intent.getSerializableExtra("cart");
        edCellphone.setText(model.getUser().getCellphone());
        edFullName.setText(model.getUser().getName());
        email = model.getUser().getEmail();
    }

    public void updateProfileDetails(View view) {
        Intent data = getIntent();
        CartModel model = (CartModel) data.getSerializableExtra("cart");
        UserModel user = model.getUser();
        String name = edFullName.getText().toString().trim();
        String cellphone = edCellphone.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter full name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cellphone)) {
            Toast.makeText(this, "Enter cellphone number", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setCellphone(cellphone);
        user.setName(name);
        FirebaseAPI.getInstance().editUser(user, bool -> {
            if (bool) {
                model.setUser(user);
                Intent intent = new Intent(ProfileDetailsActivity.this, MainActivity.class);
                intent.putExtra("cart", model);
                startActivity(intent);
            }
        });
    }
}