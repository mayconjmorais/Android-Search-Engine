package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        boolean isTable = findViewById(R.id.frameLayout) != null;

        if (isTable) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new DetailsFragment())
                    .commit();
        } else {
            Intent nextActivity = new Intent(EmptyActivity.this, EmptyActivity.class);
            //nextActivity.putExtra(datatopass);
            startActivity(nextActivity);
        }
    }
}