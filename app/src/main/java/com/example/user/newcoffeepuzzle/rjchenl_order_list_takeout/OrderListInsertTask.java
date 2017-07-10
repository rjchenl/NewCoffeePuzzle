package com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout;

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
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by user on 2017/7/8.
 */

public class OrderListInsertTask extends AsyncTask<Object,Integer,List<OrderListVO>> {
    private final static String TAG = "OrderListInsertTask";
    private final static String ACTION = "orderListInsert";



    @Override
    protected List<OrderListVO> doInBackground(Object... params) {
        String url = params[0].toString();
        String mem_id= params[1].toString();
        String store_id = params[2].toString();
        Integer ord_total = (Integer) params[3];
        Integer ord_pick = (Integer) params[4];
        String ord_add = params[5].toString();
        Integer ord_shipping = (Integer) params[6];
        Timestamp ord_time = (Timestamp) params[7];
        Integer score_seller = (Integer) params[8];


        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);

        OrderListVO orderlistvo = new OrderListVO(mem_id,store_id,ord_total,ord_pick,ord_add,ord_shipping,ord_time,score_seller);
        jsonObject.addProperty("OrderListVO",new Gson().toJson(orderlistvo));

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<OrderListVO>>() {
        }.getType();

        return gson.fromJson(jsonIn, listType);
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
        Log.d(TAG, "jsonIn:(receive response from servlet) " + jsonIn);
        return jsonIn.toString();

    }

}
