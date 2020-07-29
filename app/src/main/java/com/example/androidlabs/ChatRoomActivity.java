package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private AppCompatActivity parentActivity;
    private Bundle dataToPass;
    private DetailsFragment dFragment;
    private ListView listView;
    private MyOpener dbOpener;
    private Button hide;
    public static final String ITEM_MESSAGE = "MESSAGE";
    public static final String ITEM_SIDE = "SIDE";
    public static final String ITEM_ID = "ID";

    private ChatRoomAdapter myAdapter;
    private ArrayList<Message> elements = new ArrayList<>();
    SQLiteDatabase db;
    DetailsFragment loadedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView myList = findViewById(R.id.theListView);
        EditText message = findViewById(R.id.chatMessage);
        myList.setAdapter(myAdapter = new ChatRoomAdapter(elements));
        hide = findViewById(R.id.hideButton);

        loadDataFromDatabase();

        boolean isTable = findViewById(R.id.frameLayout) != null;

        myList.setOnItemClickListener((list, view, position, id) -> {
            dFragment = new DetailsFragment(); //add a DetailFragment
            dataToPass = new Bundle();
            dataToPass.putString(ITEM_MESSAGE, elements.get(position).getMessage());
            dataToPass.putBoolean(ITEM_SIDE, elements.get(position).isSide());
            dataToPass.putLong(ITEM_ID, id);

            if (isTable) {
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, loadedFragment = dFragment)
                        .commit();
            } else {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                //dFragment.setArguments(dataToPass);
                startActivity(nextActivity);
            }
        });

        // bandit button
        Button send = findViewById(R.id.senderButton);
        send.setOnClickListener(sd -> {
            String messageText = message.getText().toString();
            if (messageText != null && !messageText.isEmpty()) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();

                //put string sender in the SENDER column:
                newRowValues.put(MyOpener.COL_SENDER, true);

                //put string message in the MSG column:
                newRowValues.put(MyOpener.COL_MSG, messageText);

                //Now insert in the database:
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
                Message newMessage = new Message(messageText, true, newId);
                elements.add(newMessage);
                myAdapter.notifyDataSetChanged();
                //should insert the message into the database
                message.setText("");
            }
        });

        // Soldier button
        Button receive = findViewById(R.id.receiverButton);
        receive.setOnClickListener(rc -> {
            String messageText = message.getText().toString();
            if (messageText != null && !messageText.isEmpty()) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();

                //put string sender in the SENDER column:
                newRowValues.put(MyOpener.COL_SENDER, false);

                //put string message in the MSG column:
                newRowValues.put(MyOpener.COL_MSG, messageText);

                //Now insert in the database:
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                Message newMessage = new Message(messageText, false, newId);
                elements.add(newMessage);

                myAdapter.notifyDataSetChanged();
                //should insert the message into the database
                message.setText("");
            }
        });

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
                        deleteMessage(elements.get(pos));

                       if(loadedFragment != null) // remove fragment when message was deleted
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(loadedFragment)
                                .commit();
                        loadedFragment = null;
                        elements.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setView(newView)
                    .create().show();

            // alert.show();
            return false;
        });

/*      update - remove this part from layout
        // Refresh function
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresher);
        refreshLayout.setOnRefreshListener(() -> {
            //on refresh function()
            myAdapter.notifyDataSetChanged(); //rebuild list
            refreshLayout.setRefreshing(false);
        });*/
    }

    private void loadDataFromDatabase() {
        dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_SENDER, MyOpener.COL_MSG};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int messageColIndex = results.getColumnIndex(MyOpener.COL_MSG);
        int senderColIndex = results.getColumnIndex(MyOpener.COL_SENDER);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        while (results.moveToNext()) {

            String message = results.getString(messageColIndex);
            String sender = results.getString(senderColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new Message(message, (sender.equals("1") ? true : false), id));
        }
        myAdapter.notifyDataSetChanged();

        // used for debug purposes for your final project
        dbOpener.printCursor(results, dbOpener.VERSION_NUM);
    }

    protected void deleteMessage(Message m) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(m.getId())});
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
}