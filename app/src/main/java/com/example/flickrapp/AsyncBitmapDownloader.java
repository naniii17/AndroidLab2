package com.example.flickrapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class AsyncBitmapDownloader extends AsyncTask<String, Void, Bitmap>
{

    @Override
    protected Bitmap doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap bm = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            bm = BitmapFactory.decodeStream(is);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
        return bm;

    }

}
