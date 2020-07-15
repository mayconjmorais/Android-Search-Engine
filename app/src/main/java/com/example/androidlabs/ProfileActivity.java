package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageButton = null;
    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.e(ACTIVITY_NAME, "In Function onCreate()");

        // receiving email from MainActivity
        Intent fromMain = getIntent();
        String PreviousEmail = fromMain.getStringExtra("EMAIL");

        // setting EditText email into xml
        EditText typeYourEmail = findViewById(R.id.typeYourEmail);
        Button clickWeather = findViewById(R.id.btWeather);
        Button clickChat = findViewById(R.id.clickChat);

        typeYourEmail.setText(PreviousEmail);

        mImageButton = findViewById(R.id.picture);

        mImageButton.setOnClickListener(bt -> {
            dispatchTakePictureIntent();
        });

        // Lab 4
        clickChat.setOnClickListener(cc -> {
            Intent goToChat  = new Intent(this, ChatRoomActivity.class);
            startActivity(goToChat);
        });

        // Lab 6
        clickWeather.setOnClickListener(cw -> {
            Intent goToWeather  = new Intent(this, WeatherForecast.class);
            startActivity(goToWeather);
        });

    }
    // creates a new Intent which will open the camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In Function onActivityResult()");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //ImageView imageView = (ImageView) findViewById(R.id.picture);
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In Function onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In Function onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In Function onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In Function onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In Function onDestroy()");
    }
}