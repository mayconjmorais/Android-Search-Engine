package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid); // showed on my screen

        // toast message on checkBox
        Button button = (Button)findViewById(R.id.clickHere);
        button.setOnClickListener((v) ->
        {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
        } );

        // snack bar for checkbox and switch components
        CheckBox chBox = (CheckBox)findViewById(R.id.checkHere);
        chBox.setOnCheckedChangeListener((cb, b) -> {
            Snackbar.make(cb, cb.isChecked()?getResources().getString(R.string.snack_message)+" on":getResources().getString(R.string.snack_message)+" off", Snackbar.LENGTH_LONG)
                    .setAction("UNDO",  v -> cb.setChecked(!b))
                    .show();
        });

        Switch simpleSwitch  = (Switch)findViewById(R.id.switch1);
        // check state of Switch
        Boolean switchState = simpleSwitch.isChecked();

        simpleSwitch.setOnCheckedChangeListener((cb, b) -> {
            Snackbar.make(simpleSwitch, cb.isChecked()?getResources().getString(R.string.snack_message)+" on":getResources().getString(R.string.snack_message)+" off", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", v -> simpleSwitch.setChecked(!b))
                    .show();
        });

                TextView tv = findViewById(R.id.textView);
                ImageButton imageButton = findViewById(R.id.imageButton);
                EditText editText = findViewById(R.id.typeText);
    }
}