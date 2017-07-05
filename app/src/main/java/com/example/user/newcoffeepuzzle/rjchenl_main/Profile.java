package com.example.user.newcoffeepuzzle.rjchenl_main;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2017/6/29.
 */

public class Profile {

    private SharedPreferences sharedPreferences;

    public Profile(Context context){
        sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }

    public void setMemId(String mem_id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_id", mem_id);
        editor.apply();
    }

    public String getMemId(){
        return  sharedPreferences.getString("mem_id","");
    }

    public void setMem_acct(String mem_acct){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_acct",mem_acct);
        editor.apply();
    }
    public String getMem_acct(){
        return sharedPreferences.getString("mem_acct","");
    }




}
