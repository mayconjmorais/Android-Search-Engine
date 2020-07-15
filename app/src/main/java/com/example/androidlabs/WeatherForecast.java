package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    ImageView ivImage;
    TextView tvMin;
    TextView tvMax;
    TextView tvCurrent;
    TextView tvUv;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ivImage = findViewById(R.id.imgTemperature);
        tvMin = findViewById(R.id.minTemp);
        tvMax = findViewById(R.id.maxTemp);
        tvCurrent = (TextView) findViewById(R.id.currentTemp);
        tvUv = findViewById(R.id.uvRating);
        progressBar = findViewById(R.id.progressBar);


        ForecastQuery thread = new ForecastQuery();
        thread.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min, max, currentTemp, ic;
        double uv;
        String result = "Done";
        Bitmap image = null;

        protected String doInBackground(String... args) {

            HttpURLConnection conn;
            InputStream inputStream;
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int type = xpp.getEventType();
                while (type != XmlPullParser.END_DOCUMENT) {
                    if (type == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);

                            Log.i("XML minTemp:", min);
                            Log.i("XML maxTemp:", max);
                        }

                        if (xpp.getName().equals("weather")) {
                            ic = xpp.getAttributeValue(null, "icon");
                            String iconFile = ic.concat(".png");

                            if (fileExistance(iconFile)){
                                inputStream = null;
                                try{
                                    inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(inputStream);
                                Log.i("XML image", "Image already exists");
                            }else{
                                URL iconUrl = new URL("http://openweathermap.org/img/w/".concat(ic).concat(".png"));
                                image = getImage(iconUrl);
                                FileOutputStream outputStream = openFileOutput(ic + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                Log.i("XML image", "Adding new image");
                            }
                            Log.i("XML image", "file name="+iconFile);
                            publishProgress(100);
                        }
                    }
                    type = xpp.next();
                }

                //JSON UV
                 uv = getUvRating(new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //Type 2
        public void onProgressUpdate(Integer... args) {
            // use progressBar here
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            String degree = Character.toString((char) 0x00B0);
            tvCurrent.setText(tvCurrent.getText()+currentTemp+degree+"C");
            tvMin.setText(tvMin.getText()+min+degree+"C");
            tvMax.setText(tvMax.getText()+max+degree+"C");
            tvUv.setText(String.valueOf(uv));
            ivImage.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);

        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        protected Bitmap getImage(URL url) {
            HttpURLConnection iconConn = null;
            try {
                iconConn = (HttpURLConnection) url.openConnection();
                iconConn.connect();
                int response = iconConn.getResponseCode();
                if (response == 200) {
                    return BitmapFactory.decodeStream(iconConn.getInputStream());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected double getUvRating(URL url) {
            double uvRating = 0;

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                uvRating = uvReport.getDouble("value");

                Log.i("JSON uvRating", "The uv is now: " + uvRating) ;
            } catch (Exception e) {
                Log.i("JSON uvRating", "uv Error");
                e.printStackTrace();
            }
            return uvRating;
        }
    }
}