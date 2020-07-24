package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    private TextView message;
    private long  id;
    private TextView idView;
    private    CheckBox checkBox;
    private AppCompatActivity parentActivity;
    private Button hideButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View backupView = inflater.inflate(R.layout.fragment_details, container, false);

        message = (TextView) backupView.findViewById(R.id.textMessage);
        idView = (TextView) backupView.findViewById(R.id.textId);
        idView.setText("ID=" + id);
        checkBox = (CheckBox) backupView.findViewById(R.id.checkbox);


        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_MESSAGE));
        if (getArguments().getBoolean(ChatRoomActivity.ITEM_SIDE)){
            checkBox.setChecked(true);
        }

        hideButton = (Button)backupView.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return backupView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}