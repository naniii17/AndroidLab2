package com.example.flickrapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncFlickrJSONData extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity myActivity;

    public AsyncFlickrJSONData(AppCompatActivity mainActivity) {
        myActivity = mainActivity;
    }




    @Override
    protected JSONObject doInBackground(String... strings) {

        String s = null;

        URL newURL;
        HttpURLConnection connexion = null;

        try {
           newURL = new URL(strings[0]);

            connexion = (HttpURLConnection) newURL.openConnection();


            InputStream in = new BufferedInputStream(connexion.getInputStream());
            s = readStream(in);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connexion != null)
            {
                connexion.disconnect();
            }
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try{
            JSONArray items = jsonObject.getJSONArray("items");



                JSONObject json = items.getJSONObject(0);
                String UrlPhoto = json.getJSONObject("media").getString("m");
                Log.i("CIO", "URL media: " + UrlPhoto);

            AsyncBitmapDownloader asyncBitmapDownloader = new AsyncBitmapDownloader();
            changebmInView(asyncBitmapDownloader.doInBackground(UrlPhoto));

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();


        String stringextract = sb.substring("jsonFlickrFeed(".length(), sb.length() - 1);

        return stringextract;
    }

    private void changebmInView(Bitmap bm)
    {
        ImageView iv = (ImageView)myActivity.findViewById(R.id.Image);
        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv.setImageBitmap(bm);
            }
        });

    }
}
