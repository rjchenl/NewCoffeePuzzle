package com.example.user.newcoffeepuzzle.ming_Orderdetail;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Orderdetail_GetAll extends AsyncTask<Object,Integer,List<OrderdetailVO>> {
    private final static String TAG = "Orderdetail_GetAll";
    private final static String ACTION = "getOrderdetail";

    @Override
    protected List<OrderdetailVO> doInBackground(Object... params) {
        Log.d(TAG, "doInBackground: (step1_1)");
        String url = params[0].toString();
        String store_id = params[1].toString();
        String ord_id = params[2].toString();
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("store_id", store_id);
        jsonObject.addProperty("ord_id", ord_id);

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
            Log.d(TAG, "doInBackground: (step1_2)");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            Log.d(TAG, "doInBackground: (step1_3)");
            return null;
        }
        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type listType = new TypeToken<List<OrderdetailVO>>() {}.getType();

        return gson.fromJson(jsonIn, listType);
    }

    private String getRemoteData(String url, String jsonOut) throws IOException{
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn: " + jsonIn);
        return jsonIn.toString();

    }
}
