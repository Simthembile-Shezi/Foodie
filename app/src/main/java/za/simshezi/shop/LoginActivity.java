package za.simshezi.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import za.simshezi.shop.api.FirebaseAPI;
import za.simshezi.shop.model.UserModel;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edEmail, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.edLoginEmail);
        edPassword = findViewById(R.id.edLoginPassword);

    }

    public void onForgotPasswordClicked(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void onNewAccountClicked(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {

        //startActivity(new Intent(this, MainActivity.class));
        //Simshezi8@gmail.com SimShezi
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        SharedPreferences writePreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = writePreferences.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
                        FirebaseAPI.getInstance().getCustomer(email, DocumentSnapshot -> {
                            if(DocumentSnapshot != null){
                                UserModel user = new UserModel();
                                for (QueryDocumentSnapshot document : DocumentSnapshot) {
                                    user = document.toObject(UserModel.class);
                                    user.setId(document.getId());
                                    break;
                                }
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "No account found, contact admin", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}