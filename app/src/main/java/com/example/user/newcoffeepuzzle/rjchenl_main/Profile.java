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

    public void setMem_name(String mem_name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_name",mem_name);
        editor.apply();
    }

    public String getMem_name(){
        return sharedPreferences.getString("mem_name","");
    }
    public void setMem_tel(String mem_tel){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_tel",mem_tel);
        editor.apply();
    }
    public String getMem_tel(){
        return sharedPreferences.getString("mem_tel","");
    }
    public void setMem_add(String mem_add){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_add",mem_add);
        editor.apply();
    }
    public String getMem_add(){
        return sharedPreferences.getString("mem_add","");
    }
    public void setMem_points(Integer mem_points){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mem_points",mem_points);
        editor.apply();
    }
    public Integer getMem_points(){
        return sharedPreferences.getInt("mem_points",0);
    }
    public void setMem_email (String mem_email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mem_email",mem_email);
        editor.apply();
    }
    public String getMem_email(){
        return sharedPreferences.getString("mem_email","");
    }












}
