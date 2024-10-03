package com.example.androidassignments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    // Declaring variables
   private ImageButton cameraButton;
   private Switch switchObject;
   private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);

        // Reference image, switch, and checkbox buttons
        cameraButton = findViewById(R.id.imageButton3);
        switchObject = findViewById(R.id.switch2);
        checkBox = findViewById(R.id.checkBox4);

        // Camera Button
        cameraButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 100);
            }
        });

        // Switch
        switchObject.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CharSequence text;
            int duration;
            if (isChecked) {
                text = "Switch is On";
                duration = Toast.LENGTH_SHORT;
            } else {
                text = "Switch is Off";
                duration = Toast.LENGTH_LONG;
            }
            Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
            toast.show();
        });

        // Check box
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
            builder.setMessage(R.string.dialog_message)

            .setTitle(R.string.dialog_title)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { // User clicked OK button
                    finish();
                    Log.i("ListItemsActivity", "onFinish");

                    Intent resultIntent = new Intent(  );
                    resultIntent.putExtra("Response", "Here is my response");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            })
            .show();
        });

        Log.i("ListItemsActivity", "onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cameraButton.setImageBitmap(imageBitmap);
        }
    }

    void print(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
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