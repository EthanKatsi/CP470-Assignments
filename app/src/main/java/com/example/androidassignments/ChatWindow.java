package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    // Class variables
    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private String ACTIVITY_NAME = "ChatWindow";

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing variables
        listView = findViewById(R.id.myListView);
        editText = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.send);

        ChatAdapter messageAdapter = new ChatAdapter( this );
        listView.setAdapter(messageAdapter);

        // Reading from a database file (Assignment 3)
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name: " + cursor.getColumnName(i));
        }

//        while (cursor.moveToNext()) {
//            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
//            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
//            chatMessages.add(message);
//        }

        while (cursor.moveToNext()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            chatMessages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        }

        cursor.close();


        sendButton.setOnClickListener(v -> {
            String newMessage = editText.getText().toString();

            chatMessages.add(newMessage);
            messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

            // Writing to database file
            ContentValues values = new ContentValues();
            values.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage);
            db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

            editText.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0, chatMessages);
        }

        @Override
        public int getCount() {
            return chatMessages.size();
        }

        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }
    }
}