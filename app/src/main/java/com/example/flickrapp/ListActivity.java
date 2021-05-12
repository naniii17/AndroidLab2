package com.example.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONObject;

import java.util.Vector;

public class ListActivity extends AppCompatActivity {



    ListView lw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lw = (ListView)findViewById(R.id.list);
        MyAdapter myAdapter = new MyAdapter(ListActivity.this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
           AsyncFlickrJSONDataForList asyncFlickrJSONDataForList = new AsyncFlickrJSONDataForList(ListActivity.this, myAdapter);
           JSONObject jsonObject = asyncFlickrJSONDataForList.doInBackground("https://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json");
           asyncFlickrJSONDataForList.onPostExecute(jsonObject);
           LoadListView(myAdapter);

            }
        });
        t.start();
    }

    public void LoadListView(MyAdapter adapter)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lw.setAdapter(adapter);

            }
        });
    }


    public class MyAdapter extends BaseAdapter {
        Vector<String> vector;
        Context context;


        public MyAdapter(Context c) {
            this.vector = new Vector<>();
            this.context = c;
        }

        @Override
        public int getCount() {
            return vector.size();
        }

        @Override
        public Object getItem(int i) {
            return vector.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            RequestQueue queu = MySingleton.getInstance(viewGroup.getContext()).getRequestQueue();


            //String text = (String)vector.get(i);

            if(view == null){

                //view = LayoutInflater.from(context)
                  //      .inflate(R.layout.textviewlayout, viewGroup, false);
                view = LayoutInflater.from(context)
                        .inflate(R.layout.bitmaplayout, viewGroup, false);
            }

            ImageView iv = (ImageView)view.findViewById(R.id.idImageViewfromLayout);

            Response.Listener<Bitmap> resp_listener = response -> {

                iv.setImageBitmap(response);
            };
            Response.ErrorListener errorListener = error -> {
                Log.i("error","attention il y a une erreur dans la reponse");
            };

            ImageRequest imageRequest = new ImageRequest(vector.get(i), resp_listener, 2000,2000, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,errorListener );
            queu.add(imageRequest);

            Log.i("JFL", "TODO");
            return view;
        }

        public void add(String Url)
        {
            vector.add(Url);
            Log.i("JFL", "adding this url to the adapteur :" + Url);
        }


    }
}