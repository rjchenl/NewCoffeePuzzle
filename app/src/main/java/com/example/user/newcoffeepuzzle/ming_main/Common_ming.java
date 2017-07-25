package com.example.user.newcoffeepuzzle.ming_main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2017/6/21.
 */

public class Common_ming {
    //    public static String URL = "http://192.168.196.189:8080/Spot_MySQL_Web/";
//    public static String URL = "http://10.0.2.2:8081/NewCoffeePuzzle_java/";
    public static String URL = "http://172.20.10.3:8081/NewCoffeePuzzle_java/";
//    public static String URL = "http://127.0.0.1:8081/NewCoffeePuzzle_java/";

//    public static String URL = "http://10.120.39.38:8081/NewCoffeePuzzle_java/";

//     public static String URL = "http://192.168.75.1:8081/NewCoffeePuzzle_java/";





    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
