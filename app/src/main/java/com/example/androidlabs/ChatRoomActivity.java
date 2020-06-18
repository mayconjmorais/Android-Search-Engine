package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private ChatRoomAdapter myAdapter;
    private ArrayList<Message> elements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView myList = findViewById(R.id.theListView);
        EditText message = findViewById(R.id.chatMessage);

        // girl button
        Button send = findViewById(R.id.senderButton);
        send.setOnClickListener(sd -> {
            String messageText = message.getText().toString();

            elements.add(new Message(messageText, true));
            myAdapter.notifyDataSetChanged();
            message.setText("");

        });

        // Soldier button
        Button receive = findViewById(R.id.receiverButton);
        receive.setOnClickListener(rc -> {
            String messageText = message.getText().toString();

            elements.add(new Message(messageText, false));
            myAdapter.notifyDataSetChanged();
            message.setText("");
        });

        myList.setAdapter(myAdapter = new ChatRoomAdapter(elements));

        // AlertDialog
        myList.setOnItemLongClickListener((parent, view, pos, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            String alertMsg = R.string.alertmsg1 + pos + "\n" + R.string.alertmsg2;
            alert.setTitle(R.string.alertMessage)
                    .setMessage("The selected row is: " + pos + "\nThe database id id: ")
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .setPositiveButton(R.string.yes, (click, arg) -> {
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    });
            alert.show();
            return false;
        });

        // Refresh function
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresher);
        refreshLayout.setOnRefreshListener(() -> {
            //on refresh function()
            myAdapter.notifyDataSetChanged(); //rebuild list
            refreshLayout.setRefreshing(false);
        });
    }

    public class ChatRoomAdapter extends BaseAdapter {
        ArrayList<Message> m;

        public ChatRoomAdapter(ArrayList<Message> messageList) {
            this.m = messageList;
        }

        @Override
        public int getCount() {
            return m.size();
        }

        @Override
        public String getItem(int position) {
            return m.get(position).getMessage();
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;
            TextView text;
            LayoutInflater inflater = getLayoutInflater();

            if (m.get(position).isSide()) {
                newView = inflater.inflate(R.layout.activity_chat_sender, parent, false);
                text = (TextView) newView.findViewById(R.id.textChat);
                text.setText(getItem(position));
            } else {
                newView = inflater.inflate(R.layout.activity_chat_receiver, parent, false);
                text = (TextView) newView.findViewById(R.id.textChatRec);
                text.setText(getItem(position));
            }
            return newView;
        }
    }

    public class Message {
        String message;
        boolean side;

        public Message(String message, boolean side) {
            this.message = message;
            this.side = side;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSide() {
            return side;
        }

        public void setSide(boolean side) {
            this.side = side;
        }
    }

}