package com.example.user.newcoffeepuzzle.rjchenl_member;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.user.newcoffeepuzzle.R;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class MemberImageGetTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "MemberImageGetTask";
    private final static String ACTION = "getImage";
    private final WeakReference<ImageView> imageViewWeakReference;

    public MemberImageGetTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }


    @Override
    protected Bitmap doInBackground(Object... params) {
        Log.d(TAG, "doInBackground: enter doing");
        String url = params[0].toString();
        String mem_id = params[1].toString();
        int imageSize = Integer.parseInt(params[2].toString());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("mem_id", mem_id);
        jsonObject.addProperty("imageSize", imageSize);
        Bitmap bitmap;

        try {
            bitmap = getRemoteImage(url, jsonObject.toString());
            Log.d(TAG, "doInBackground: bitmap R"+bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }



    private Bitmap getRemoteImage(String url, String jsonOut) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            Log.d(TAG, "getRemoteImage: bitmap : "+bitmap);
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap = null;
        }
        ImageView imageView = imageViewWeakReference.get();
        if(imageView !=null){
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setImageResource(R.drawable.coffeeshop_member);
            }
        }
        super.onPostExecute(bitmap);
    }



}
