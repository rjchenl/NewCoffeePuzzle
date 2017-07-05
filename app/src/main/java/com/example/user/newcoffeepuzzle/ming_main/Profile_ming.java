package com.example.user.newcoffeepuzzle.ming_main;

import android.content.Context;
import android.content.SharedPreferences;



public class Profile_ming {
    private SharedPreferences sharedPreferences;

    public Profile_ming(Context context){
        sharedPreferences = context.getSharedPreferences("Profile_ming",Context.MODE_APPEND);
    }

    public void setStoreId(String store_id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("store_id", store_id);
        editor.apply();
    }

    public String getStoreId(){
        return  sharedPreferences.getString("store_id","");
    }

    public void setStore_acct(String store_acct){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("store_acct", store_acct);
        editor.apply();
    }

    public String getStore_acct(){
        return  sharedPreferences.getString("getStore_acct","");
    }
}
