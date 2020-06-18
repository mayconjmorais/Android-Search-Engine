package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<ChatMessage> elements = new ArrayList<>();
    private ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        myList = findViewById(R.id.theListView);
        ChatRoomAdapter myAdapter = new ChatRoomAdapter(getApplicationContext(), elements);

        // AlertDialog
        myList.setOnItemLongClickListener((parent, view, pos, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            String alertMsg = R.string.alertmsg1 + pos + "\n" + R.string.alertmsg2;
            alert.setTitle(R.string.alertMessage)
                    .setMessage(R.string.alertmsg1)
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .setPositiveButton(R.string.yes, (click, arg) -> {
                        elements.remove(pos);
                     myAdapter.notifyDataSetChanged();
                    });
            alert.show();
            return false;
        });

        // girl button
        Button send = findViewById(R.id.senderButton);
        send.setOnClickListener(sd -> {
            EditText message = findViewById(R.id.chatMessage);

            // send message when type something
            if (!message.getText().toString().equals("")) {
                myList.setAdapter(myAdapter);
                elements.add(new ChatMessage(message.getText().toString(), true));
                myAdapter.notifyDataSetChanged();
                message.setText("");
            }
        });

        // Soldier button
        Button receive = findViewById(R.id.receiverButton);
        receive.setOnClickListener(rc -> {
            EditText message = findViewById(R.id.chatMessage);

            // send message when type something
            if (!message.getText().toString().equals("")) {
                myList.setAdapter(myAdapter);
                elements.add(new ChatMessage(message.getText().toString(), false));
                myAdapter.notifyDataSetChanged();
                message.setText("");
            }
        });
        // TODO fudeo
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresher);
        refreshLayout.setOnRefreshListener(() -> {
            //on refresh function()
            myAdapter.notifyDataSetChanged(); //rebuild list
            refreshLayout.setRefreshing(false);
        });
    }

    public class ChatRoomAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList chatMessage;

        public ChatRoomAdapter(Context context, ArrayList<ChatMessage> chatMessage) {
            this.context = context;
            this.chatMessage = chatMessage;
            this.inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Object getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;
            TextView  messageText;

            if ( newView == null){
                ChatMessage cm = (ChatMessage) chatMessage.get(position);
                if (cm != null ){
                    if (cm.isSender()) {
                        newView = inflater.inflate(R.layout.activity_chat_sender, null);
                        messageText = (TextView)newView.findViewById(R.id.textChat);
                    } else {
                        newView = inflater.inflate(R.layout.activity_chat_receiver, null);
                        messageText = (TextView)newView.findViewById(R.id.textChatRec);
                    }
                    messageText.setText(cm.getMessage());

                }
            }
            return newView;
        }
    }

    class ChatMessage {
        public String message;
        public boolean sender;

        public ChatMessage(String message, boolean sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSender() {
            return sender;
        }

        public void setSender(boolean sender) {
            this.sender = sender;
        }
    }

}