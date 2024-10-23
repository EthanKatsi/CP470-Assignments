package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTestToolbarBinding binding;

    private String userMessage = "Default Message";

    // The purpose of this function is to create your toolbar by inflating it from your xml file
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    // To respond to one of the items being selected
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        if (id == R.id.action_one) {
            Log.d("Toolbar", "Option 1 Selected");
            Snackbar.make(binding.fab, "You selected item 1", Snackbar.LENGTH_LONG).show();

        } else if (id == R.id.action_two) {
            Log.d("Toolbar", "Option 2 Selected");
            Snackbar.make(binding.fab, "You selected item 2", Snackbar.LENGTH_LONG).show();

            //Start an activity…
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pick_color);
            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    finish(); // finishes the activity
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog so do nothing
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.action_three) {
            Log.d("Toolbar", "Option 3 Selected");
            Snackbar.make(binding.fab, "You selected item 3", Snackbar.LENGTH_LONG).show();

            showCustomDialog();

        } else if (id == R.id.about) {
            Toast.makeText(this, "Version 1.0, by Ethan Katsiroubas", Toast.LENGTH_LONG).show();
            return true;

        } else {
            return super.onOptionsItemSelected(mi);
        }
        return true;
    }

    private void showCustomDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        final EditText editTextMessage = dialogView.findViewById(R.id.edit_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("New Message")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userMessage = editTextMessage.getText().toString();
                        Snackbar.make(binding.fab, "Message updated", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // does nothing when you click on cancel
                    }
                });
        builder.create().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Button clicked", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

}