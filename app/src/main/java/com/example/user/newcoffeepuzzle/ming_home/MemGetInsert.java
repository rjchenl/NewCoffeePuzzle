package com.example.user.newcoffeepuzzle.ming_home;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MemGetInsert extends AsyncTask<Object,Integer,Intent> {
    private final static String TAG = "MemGetInsert";
    private final static String ACTION = "getMem_Insert";
    @Override
    protected Intent doInBackground(Object... params) {
        String url = params[0].toString();
        String inser_memid = params[1].toString();
        String inser_mem_psw = params[2].toString();
        String inser_mem_name = params[3].toString();
        String inser_mem_nanber = params[4].toString();
        String inser_mem_mail = params[5].toString();
        String inser_mem_add = params[6].toString();
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("inser_memid", inser_memid);
        jsonObject.addProperty("inser_mem_psw", inser_mem_psw);
        jsonObject.addProperty("inser_mem_name", inser_mem_name);
        jsonObject.addProperty("inser_mem_nanber", inser_mem_nanber);
        jsonObject.addProperty("inser_mem_mail", inser_mem_mail);
        jsonObject.addProperty("inser_mem_add", inser_mem_add);

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
            Log.d(TAG, "doInBackground: (step1_2)");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            Log.d(TAG, "doInBackground: (step1_3)");
            return null;
        }

        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return null;
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
