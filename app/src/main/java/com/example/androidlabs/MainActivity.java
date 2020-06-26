package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_main_linear); // showed on my screen

        prefs = getSharedPreferences("fileName", MODE_PRIVATE);

        // step 5
        String savedString = prefs.getString("ReserveName", "");

        EditText emailText = findViewById(R.id.typeTextEmail);
        emailText.setText(savedString);

        EditText emailPass = findViewById(R.id.typeTextPass);

        Button loginButton = findViewById(R.id.clickLogin);
        loginButton.setOnClickListener(bt -> {
            Intent goToProfile  = new Intent(this, ProfileActivity.class); // in the calling class
            goToProfile.putExtra("EMAIL", emailText.getText().toString() );
            startActivityForResult(goToProfile, 1);
            onPause(emailText.getText().toString());

            // midterm
            Spinner optSelect = findViewById(R.id.os);

        });
    }

    protected void onPause(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }
}