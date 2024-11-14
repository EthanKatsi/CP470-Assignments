package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {
    // variables
    private ProgressBar progressBar;
    private ImageView imageViewWeather;
    private TextView currentTempTextView, minTempTextView, maxTempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        currentTempTextView = findViewById(R.id.currentTemp);
        minTempTextView = findViewById(R.id.minTemp);
        maxTempTextView = findViewById(R.id.maxTemp);

        // Loads the weather data
        new ForecastQuery().execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        // Variables from activity_weather_forecast.xml
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private String iconName;
        private Bitmap weatherImage;

        @Override
        protected String doInBackground(String... args) {
            String apiKey = "d7ee90425fc6e2e0b0e59645ee768c8f";
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d7ee90425fc6e2e0b0e59645ee768c8f&mode=xml&units=metric";
            HttpURLConnection urlConnection;
            InputStream inputStream;

            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d7ee90425fc6e2e0b0e59645ee768c8f&mode=xml&units=metric");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);

                    int type;
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                Log.i("WeatherForecast", "Current Temp: " + currentTemp);
                                publishProgress(25);

                                minTemp = parser.getAttributeValue(null, "min");
                                Log.i("WeatherForecast", "Min Temp: " + minTemp);
                                publishProgress(50);

                                maxTemp = parser.getAttributeValue(null, "max");
                                Log.i("WeatherForecast", "Max Temp: " + maxTemp);
                                publishProgress(75);

                            } else if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";
                                Log.i("WeatherForecast", "Icon Name: " + iconName);

                                if (fileExistance(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("WeatherForecast", "Found the file locally");
                                    weatherImage = BitmapFactory.decodeStream(fis);
                                } else {
                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                    weatherImage = downloadImage(new URL(iconUrl));
                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    weatherImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i("WeatherForecast", "Downloaded the file from the Internet");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    urlConnection.disconnect();
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("WeatherForecast", "Data retrieved - Current: " + currentTemp + ", Min: " + minTemp + ", Max: " + maxTemp + ", Icon: " + iconName);
            return "Weather data loaded";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("WeatherForecast", "onPostExecute called");
            progressBar.setVisibility(View.INVISIBLE);

            imageViewWeather.setImageBitmap(weatherImage);

            currentTempTextView.setText(currentTemp + "C\u00b0");

            minTempTextView.setText(minTemp + "C\u00b0");

            maxTempTextView.setText(maxTemp + "C\u00b0");
        }

        private Bitmap downloadImage(URL imageUrl) {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) imageUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else {
                    return null;
                }

            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}