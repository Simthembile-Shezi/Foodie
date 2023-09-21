package za.simshezi.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.UserModel;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edName, edEmail, edCellphone, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edName = findViewById(R.id.edSignupUserName);
        edEmail = findViewById(R.id.edSignupUserEmail);
        edCellphone = findViewById(R.id.edSignupUserCellphone);
        edPassword = findViewById(R.id.edSignupUserPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onAlreadySignedClicked(View view) {
        finish();
    }

    public void onSignupClicked(View view) {
        String name = edName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String cellphone = edCellphone.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter full name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cellphone)) {
            Toast.makeText(this, "Enter cellphone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        UserModel userModel = new UserModel(name, email, cellphone);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseAPI.getInstance().addUser(userModel, aBoolean -> {
                            if(aBoolean){
                                finish();
                            }else {
                                Toast.makeText(SignupActivity.this, "User profile not created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Registration failed
                        Toast.makeText(SignupActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}