package com.example.androidassignments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Reference login button from LoginActivity layout
        loginButton = findViewById(R.id.loginButton);
        loginName = findViewById(R.id.editTextText);

        // Read value of stored email address in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        loginName.setText(savedEmail);

        // Function for login button that stores text in login email field
        loginButton.setOnClickListener(v -> {
            String enteredEmail = loginName.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DefaultEmail", enteredEmail);
            editor.apply();

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