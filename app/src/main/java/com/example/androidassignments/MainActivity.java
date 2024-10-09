package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declaring variables
    private Button mainButton;
    String ACTIVITY_NAME = "Main Activity";
    private Button startChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Stores reference to button
        mainButton = findViewById(R.id.button);

        mainButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
            startActivityForResult(intent, 10);
        });

        // When you click Start Chat it writes information to the debug window
        startChat = findViewById(R.id.startChat);
        startChat.setOnClickListener(v -> {
            Log.i(ACTIVITY_NAME, "User Clicked Start Chat");
            Intent intent2 = new Intent(MainActivity.this, ChatWindow.class);
            startActivityForResult(intent2, 10);
        });

        Log.i("MainActivity", "onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == 10) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            if (responseCode == Activity.RESULT_OK && data != null) {
                String messagePassed = data.getStringExtra("Response");
                if (messagePassed != null) {
                    Toast toast = Toast.makeText(this, "ListItemsActivity passed: " + messagePassed, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }

    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume");
    }
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart");
    }
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause");
    }
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy");
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MainActivity", "onSaveInstanceState");
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("MainActivity", "onRestoreInstanceState");
    }
}