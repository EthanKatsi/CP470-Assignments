package com.example.androidassignments;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);

        Log.i("ListItemsActivity", "onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    protected void onResume() {
        super.onResume();
        Log.i("ListItemsActivity", "onResume");
    }
    protected void onStart() {
        super.onStart();
        Log.i("ListItemsActivity", "onStart");
    }
    protected void onPause() {
        super.onPause();
        Log.i("ListItemsActivity", "onPause");
    }
    protected void onStop() {
        super.onStop();
        Log.i("ListItemsActivity", "onStop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ListItemsActivity", "onDestroy");
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("ListItemsActivity", "onSaveInstanceState");
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("ListItemsActivity", "onRestoreInstanceState");
    }
}