package com.example.user.newcoffeepuzzle.ming_Orderlist;

import java.security.Timestamp;

/**
 * Created by Java on 2017/6/29.
 */

public class OrderlistVO {
    private String ord_id;
    private String mem_id;
    private String store_id;
    private Integer ord_total;
    private Integer ord_pick;
    private String ord_add;
    private Integer ord_shipping;
    private String ord_time;
    private Integer score_seller;


    public Integer getScore_seller() {
        return score_seller;
    }

    public void setScore_seller(Integer score_seller) {
        this.score_seller = score_seller;
    }

    public OrderlistVO(Integer score_seller) {

        this.score_seller = score_seller;
    }

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

    public Integer getOrd_total() {
        return ord_total;
    }

    public void setOrd_total(Integer ord_total) {
        this.ord_total = ord_total;
    }

    public Integer getOrd_pick() {
        return ord_pick;
    }

    public void setOrd_pick(Integer ord_pick) {
        this.ord_pick = ord_pick;
    }

    public String getOrd_add() {
        return ord_add;
    }

    public void setOrd_add(String ord_add) {
        this.ord_add = ord_add;
    }

    public Integer getOrd_shipping() {
        return ord_shipping;
    }

    public void setOrd_shipping(Integer ord_shipping) {
        this.ord_shipping = ord_shipping;
    }

    public String getOrd_time() {
        return ord_time;
    }

    public void setOrd_time(String ord_time) {
        this.ord_time = ord_time;
    }

    public OrderlistVO(String ord_id, String mem_id, String store_id, Integer ord_total, Integer ord_pick, String ord_add, Integer ord_shipping, String ord_time) {

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
