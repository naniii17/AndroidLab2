package com.example.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button butonGetImage;
    ImageView imageAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butonGetImage = (Button)findViewById(R.id.idButonGetImage);
        butonGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AsyncFlickrJSONData asyncTask = new AsyncFlickrJSONData(MainActivity.this);
                        JSONObject json = asyncTask.doInBackground("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json");
                        asyncTask.onPostExecute(json);

                    }
                });
                thread.start();

            }
        });
    }


    public void redirection(View view) {
        Intent intent = new Intent(MainActivity.this,
                ListActivity.class);
        startActivity(intent);
    }
}