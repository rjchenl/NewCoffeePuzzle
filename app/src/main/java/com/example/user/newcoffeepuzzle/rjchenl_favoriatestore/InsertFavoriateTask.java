package com.example.user.newcoffeepuzzle.rjchenl_favoriatestore;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2017/7/18.
 */

public class InsertFavoriateTask extends AsyncTask<Object,Integer,String> {
    private final static String TAG = "InsertFavoriateTask";
    private final static String ACTION = "insertCombination";



    @Override
    protected String doInBackground(Object... params) {
        String url = params[0].toString();
        Fav_storeVO fav_storevo = (Fav_storeVO) params[1];

//        String mem_id = params[1].toString();
//        String store_id = params[2].toString();
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("fav_storevo", new Gson().toJson(fav_storevo));




        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


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
        Log.d(TAG, "jsonIn:(receive response from servlet) isExistCheck " + jsonIn);
        return jsonIn.toString();

    }
}
