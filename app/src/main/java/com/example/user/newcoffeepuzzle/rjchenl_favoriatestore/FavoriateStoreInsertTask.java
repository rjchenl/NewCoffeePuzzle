package com.example.user.newcoffeepuzzle.rjchenl_favoriatestore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.user.newcoffeepuzzle.rjchenl_activities.ActivityVO;
import com.google.gson.Gson;
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

/**
 * Created by user on 2017/6/29.
 */

public class FavoriateStoreInsertTask extends AsyncTask<Object,Integer,String> {
    private final static String TAG = "FavoriateStoreInsertTask";
    private final static String ACTION = "fav_sotreInsert";


    @Override
    protected String doInBackground(Object... params) {
        String url = params[0].toString();
        String mem_id = params[1].toString();
        String store_id = params[2].toString();

        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }

//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Fav_storeVO>>() {
//        }.getType();
//        return gson.fromJson(jsonIn, listType);
        return jsonIn;
    }


    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();//取得回應用的
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut(request action from app): " + jsonOut);
        //{"action":"getAll"}
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
        Log.d(TAG, "jsonIn:(receive response from servlet) RJJ " + jsonIn);
        return jsonIn.toString();

    }

}
