package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by user on 2017/6/27.
 */

public class StoreGetAllTask extends AsyncTask<Object,Integer,List<StoreVO>>{
    private final static String TAG = "StoreGetAllTask";
    private final static String ACTION = "getAll";
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }



    interface Listener {
        void onGetStoresDone(List<StoreVO> storeVOs);
    }
    @Override
    protected List<StoreVO> doInBackground(Object[] params) {
        String url = params[0].toString();
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action",ACTION);

        try {
            jsonIn = getRemoteData(url,jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG,e.toString());
            return null;
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<StoreVO>>(){}.getType();

        return gson.fromJson(jsonIn,listType);
    }


    @Override
    protected void onPostExecute(List<StoreVO> storeVOs) {
        super.onPostExecute(storeVOs);
        Log.d(TAG, "onPostExecute: storeVOs : "+storeVOs);
        Log.d(TAG, "onPostExecute: listener : "+listener);
//        listener.onGetStoresDone(storeVOs);
    }


    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset","UTF-8");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "getRemoteData:(request action from app) "+jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
                while((line = br.readLine()) != null) {
                    jsonIn.append(line);
                }
            }else{
                Log.d(TAG, "getRemoteData: "+responseCode);
            }
        connection.disconnect();
        Log.d(TAG, "jsonIn:(receive response from servlet )"+jsonIn);
        return jsonIn.toString();

    }


}
