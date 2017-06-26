package com.example.user.newcoffeepuzzle.take_orders;

import java.security.Timestamp;

/**
 * Created by Java on 2017/6/26.
 */

public class Take_OrdersVO {
    private String ord_id;
    private String mem_id;
    private String store_id;
    private int ord_total;
    private int ord_pick;
    private String ord_add;
    private int ord_shipping;
    private Timestamp ord_time;

    public String getOrd_id() {
        return ord_id;
    }

    public void setOrd_id(String ord_id) {
        this.ord_id = ord_id;
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

    public int getOrd_total() {
        return ord_total;
    }

    public void setOrd_total(int ord_total) {
        this.ord_total = ord_total;
    }

    public int getOrd_pick() {
        return ord_pick;
    }

    public void setOrd_pick(int ord_pick) {
        this.ord_pick = ord_pick;
    }

    public String getOrd_add() {
        return ord_add;
    }

    public void setOrd_add(String ord_add) {
        this.ord_add = ord_add;
    }

    public int getOrd_shipping() {
        return ord_shipping;
    }

    public void setOrd_shipping(int ord_shipping) {
        this.ord_shipping = ord_shipping;
    }

    public Timestamp getOrd_time() {
        return ord_time;
    }

    public void setOrd_time(Timestamp ord_time) {
        this.ord_time = ord_time;
    }

    public Take_OrdersVO(String ord_id, String mem_id, String store_id, int ord_total, int ord_pick, String ord_add, int ord_shipping, Timestamp ord_time) {
        this.ord_id = ord_id;
        this.mem_id = mem_id;
        this.store_id = store_id;
        this.ord_total = ord_total;
        this.ord_pick = ord_pick;
        this.ord_add = ord_add;
        this.ord_shipping = ord_shipping;
        this.ord_time = ord_time;
    }
}
