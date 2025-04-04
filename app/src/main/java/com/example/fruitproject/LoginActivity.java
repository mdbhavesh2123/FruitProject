package com.example.fruitproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerButton;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        // Check if user is already logged in
//        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
//        boolean isNewUser = sharedPreferences.getBoolean("isNewUser", true);
//
//        if (isLoggedIn) {
//            // Redirect to HomeActivity if the user is already logged in
//            startActivity(new Intent(this, HomeActivity.class));
//            finish();
//            return;
//        } else if (isNewUser) {
//            // Redirect to RegisterActivity if the user is new
//            startActivity(new Intent(this, RegisterActivity.class));
//            finish();
//            return;
//        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if user is already logged in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(this, HomeActivity.class));
//            finish();
//            return;
//        }

//        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Set click listeners
        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
//        }
//    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Disable login button while signing in
        loginButton.setEnabled(false);
//        loginButton.setEnabled(false);

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Sign in success
//                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        finish();
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(LoginActivity.this, "Authentication failed. Please register if you're a new user.",
//                                Toast.LENGTH_LONG).show();
//                        loginButton.setEnabled(true);
//                    }
//                });
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                       //updateLoginStatus();
                        // Login successful, go to HomeActivity
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please register if you're a new user.",
                                Toast.LENGTH_LONG).show();
                        loginButton.setEnabled(true);
                    }
                });
    }
//    private void updateLoginStatus() {
//        // Update SharedPreferences to mark user as logged in
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isLoggedIn", true);
//        editor.putBoolean("isNewUser", false);
//        editor.apply();
//    }
}