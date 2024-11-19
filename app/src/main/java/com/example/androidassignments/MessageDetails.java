package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class MessageDetails extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        String message = getIntent().getStringExtra("message");
        long id = getIntent().getLongExtra("id", -1);

        Log.d("MessageDetails", "Message: " + message + ", ID: " + id);

        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putLong("id", id);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.messageDetailsContainer, fragment)
                .commit();
    }

    public static class MessageFragment extends Fragment {
        private ChatWindow chatWindow;

        public void setChatWindow(ChatWindow chatWindow) {
            this.chatWindow = chatWindow;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_message_details, container, false);

            TextView messageTextView = view.findViewById(R.id.messageTextView);
            TextView idTextView = view.findViewById(R.id.idTextView);
            Button deleteButton = view.findViewById(R.id.deleteMessageButton);

            if (getArguments() != null) {
                String message = getArguments().getString("message", "No message provided");
                long id = getArguments().getLong("id", -1);

                messageTextView.setText(message);
                idTextView.setText("ID = " + id);

                deleteButton.setOnClickListener(v -> {
                    if (chatWindow != null) {
                        chatWindow.deleteMessageById(id);
                        getParentFragmentManager().beginTransaction().remove(this).commit();
                    } else {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", id);
                        requireActivity().setResult(RESULT_OK, resultIntent);
                        requireActivity().finish();
                    }
                });
            }
            return view;
        }
    }
}
