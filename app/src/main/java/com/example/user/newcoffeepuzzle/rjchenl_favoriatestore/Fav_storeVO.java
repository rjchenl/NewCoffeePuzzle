package com.example.user.newcoffeepuzzle.rjchenl_favoriatestore;

import java.io.Serializable;

/**
 * Created by user on 2017/6/29.
 */

public class Fav_storeVO implements Serializable {

    private String mem_id;
    private String store_id;
    private String store_name;
    private String store_add;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_add() {
        return store_add;
    }

    public void setStore_add(String store_add) {
        this.store_add = store_add;
    }

    public Fav_storeVO(){}

    public Fav_storeVO(String mem_id) {
        this.mem_id = mem_id;
    }

    public Fav_storeVO(String mem_id, String store_id){
        this.mem_id = mem_id;
        this.store_id = store_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

}