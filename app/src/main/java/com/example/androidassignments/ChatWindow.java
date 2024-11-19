package com.example.androidassignments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final int DELETE_MESSAGE_REQUEST = 1;

    // Class variables
    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private String ACTIVITY_NAME = "ChatWindow";

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ChatAdapter messageAdapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);

        boolean isTabletLayout = findViewById(R.id.tablet_frame) != null;

        listView = findViewById(R.id.myListView);
        editText = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.send);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        cursor = db.query(ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            chatMessages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMessage = chatMessages.get(position);
            long messageId = messageAdapter.getItemId(position);

            if (isTabletLayout) {
                // Tablet/Landscape Mode on phone
                MessageDetails.MessageFragment fragment = new MessageDetails.MessageFragment();
                fragment.setChatWindow(this);
                Bundle args = new Bundle();
                args.putString("message", selectedMessage);
                args.putLong("id", messageId);
                fragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.tablet_frame, fragment);
                transaction.commit();
            } else {
                // Phone
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtra("message", selectedMessage);
                intent.putExtra("id", messageId);
                startActivityForResult(intent, DELETE_MESSAGE_REQUEST);
            }
        });

        sendButton.setOnClickListener(v -> {
            String newMessage = editText.getText().toString();
            ContentValues values = new ContentValues();
            values.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage);
            db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

            cursor = db.query(ChatDatabaseHelper.TABLE_NAME,
                    new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                    null, null, null, null, null);

            chatMessages.clear();
            while (cursor.moveToNext()) {
                chatMessages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            }

            messageAdapter.notifyDataSetChanged();
            editText.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DELETE_MESSAGE_REQUEST && resultCode == RESULT_OK) {
            long messageIdToDelete = data.getLongExtra("id", -1);
            if (messageIdToDelete != -1) {
                deleteMessageById(messageIdToDelete);
            }
        }
    }

    @SuppressLint("Range")
    public void deleteMessageById(long id) {
        db.delete(ChatDatabaseHelper.TABLE_NAME,
                ChatDatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        chatMessages.clear();
        cursor = db.query(ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            chatMessages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        }

        messageAdapter.notifyDataSetChanged();

        // Remove the fragment if running on a tablet
        if (findViewById(R.id.tablet_frame) != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.tablet_frame)).commit();
        }
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

        @SuppressLint("Range")
        @Override
        public long getItemId(int position) {
            if (cursor != null && cursor.moveToPosition(position)) {
                return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            }
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}
