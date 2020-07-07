package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView myList = findViewById(R.id.theListView);
        EditText message = findViewById(R.id.chatMessage);

        loadDataFromDatabase();

        // bandit button
        Button send = findViewById(R.id.senderButton);
        send.setOnClickListener(sd -> {
            String messageText = message.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //put string sender in the SENDER column:
            newRowValues.put(MyOpener.COL_SENDER, "user1");

            //put string message in the MSG column:
            newRowValues.put(MyOpener.COL_MSG, "messageText");

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Message newMessage = new Message(messageText, true, newId);
            elements.add(newMessage);
            myAdapter.notifyDataSetChanged();
            //should insert the message into the database
            message.setText("");

        });

        // Soldier button
        Button receive = findViewById(R.id.receiverButton);
        receive.setOnClickListener(rc -> {
            String messageText = message.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //put string sender in the SENDER column:
            newRowValues.put(MyOpener.COL_SENDER, "user2");

            //put string message in the MSG column:
            newRowValues.put(MyOpener.COL_MSG, "messageText");

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message newMessage = new Message(messageText, false, newId);
            elements.add(newMessage);

            myAdapter.notifyDataSetChanged();
            //should insert the message into the database
            message.setText("");
        });

        myList.setAdapter(myAdapter = new ChatRoomAdapter(elements));

        // AlertDialog
        myList.setOnItemLongClickListener((parent, view, pos, id) -> {
            // professor example - does not work yet
            View newView = getLayoutInflater().inflate(R.layout.activity_chat_sender, null, false);
            TextView text = newView.findViewById(R.id.textChat);
            text.setText("Different layout");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.alertMessage)
                    .setMessage("Do you want to delete this?")
                    //.setMessage("The selected row is: " + pos + "\nThe database id id: "+id)
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .setPositiveButton(R.string.yes, (click, arg) -> {
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setView(newView)
                    .create().show();

                   // alert.show();
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

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_SENDER, MyOpener.COL_MSG};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int messageColIndex = results.getColumnIndex(MyOpener.COL_MSG);
        int senderColIndex = results.getColumnIndex(MyOpener.COL_SENDER);
        int idColIndex = results.getColumnIndex(MyOpener.COL_SENDER);

        while (results.moveToNext()) {
            String message = results.getString(messageColIndex);
            boolean sender = true;
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new Message(message, sender, id));
            myAdapter.notifyDataSetChanged();
        }
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
            return m.get(position).getId();
            //return (long) position;
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
        long id;
        String message;
        boolean side;

        public Message(String message, boolean side, long id) {
            this.id = id;
            this.message = message;
            this.side = side;
        }

        public long getId() {
            return id;
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