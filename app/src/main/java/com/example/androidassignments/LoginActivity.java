package com.example.androidassignments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    // Declaring Variables
    private Button loginButton;
    private EditText loginName;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Reference buttons and edit texts from LoginActivity layout
        loginButton = findViewById(R.id.loginButton);
        loginName = findViewById(R.id.editTextText);
        passwordField = findViewById(R.id.editTextText2);

        // Read value of stored email address in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        loginName.setText(savedEmail);

        // Function for login button that stores text in login email field
        loginButton.setOnClickListener(v -> {
            // Checks empty passwords and not properly formatted emails
            String enteredEmail = loginName.getText().toString();
            String enteredPassword = passwordField.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (enteredPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            enteredEmail = loginName.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DefaultEmail", enteredEmail);
            editor.apply();

            // Intent moves pages from LoginActivity to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Log.i("loginActivity", "onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("loginActivity", "onResume");
    }
    protected void onStart() {
        super.onStart();
        Log.i("loginActivity", "onStart");
    }
    protected void onPause() {
        super.onPause();
        Log.i("loginActivity", "onPause");
    }
    protected void onStop() {
        super.onStop();
        Log.i("loginActivity", "onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i("loginActivity", "onDestroy");
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("loginActivity", "onSaveInstanceState");
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("loginActivity", "onRestoreInstanceState");
    }
}