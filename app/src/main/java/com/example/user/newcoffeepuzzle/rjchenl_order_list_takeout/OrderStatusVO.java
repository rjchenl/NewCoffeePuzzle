package com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by user on 2017/7/11.
 */

public class OrderStatusVO implements Serializable {

    String ord_id;
    String store_name;
    Integer ord_pick;
    Integer ord_shipping;
    Timestamp ord_time;
    String mem_name;
    String ord_add;
    String store_id;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getOrd_add() {
        return ord_add;
    }

    public void setOrd_add(String ord_add) {
        this.ord_add = ord_add;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public Integer getOrd_total() {
        return ord_total;
    }

    public void setOrd_total(Integer ord_total) {
        this.ord_total = ord_total;
    }

    Integer ord_total;


    public String getOrd_id() {
        return ord_id;
    }

    public void setOrd_id(String ord_id) {
        this.ord_id = ord_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Integer getOrd_pick() {
        return ord_pick;
    }

    public void setOrd_pick(Integer ord_pick) {
        this.ord_pick = ord_pick;
    }

    public Integer getOrd_shipping() {
        return ord_shipping;
    }

    public void setOrd_shipping(Integer ord_shipping) {
        this.ord_shipping = ord_shipping;
    }

    public Timestamp getOrd_time() {
        return ord_time;
    }

    public void setOrd_time(Timestamp ord_time) {
        this.ord_time = ord_time;
    }
}
