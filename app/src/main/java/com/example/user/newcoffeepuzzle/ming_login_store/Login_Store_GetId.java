package com.example.user.newcoffeepuzzle.ming_login_store;

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


public class Login_Store_GetId extends AsyncTask<Object,Integer,Login_StoreVO> {
    private final static String TAG = "Login_Store_GetId";
    private final static String ACTION = "findByStore";

    @Override
    protected Login_StoreVO doInBackground(Object[] params) {
        Log.d(TAG, "doInBackground: (step1_1)");
        String url = params[0].toString();
        String store_acct = params[1].toString();
        String store_pwd = params[2].toString();
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("store_acct",store_acct);
        jsonObject.addProperty("store_pwd",store_pwd);
        try {
            jsonIn = getRemoteData(url, jsonObject.toString());

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        
            return null;
        }

        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        Type listType = new TypeToken<List<Login_StoreVO>>() {}.getType();
        Log.d(TAG, "doInBackground: jsonIn"+jsonIn);
//        Log.d(TAG, "doInBackground: listType"+listType);
        return gson.fromJson(jsonIn, Login_StoreVO.class);
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
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
