package com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist;

import java.io.Serializable;
import java.sql.Date;

public class SpndcoffeeVO implements Serializable {

    private String spnd_id;
    private String store_id;
    private String spnd_name;
    private String spnd_prod;
    private String spnd_enddate;
    private Integer spnd_amt;
    private byte[] spnd_img;
    private String store_name;
    private String mem_id;
    private String mem_name;




    public SpndcoffeeVO(){}

    public SpndcoffeeVO(String spnd_id, String store_id, String spnd_name, String spnd_prod, String spnd_enddate, Integer spnd_amt, byte[] spnd_img){
        this.spnd_id = spnd_id;
        this.store_id = store_id;
        this.spnd_name = spnd_name;
        this.spnd_prod = spnd_prod;
        this.spnd_enddate = spnd_enddate;
        this.spnd_amt = spnd_amt;
        this.spnd_img = spnd_img;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getStore_name() {
        return store_name;
    }


    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getSpnd_id() {
        return spnd_id;
    }

    public void setSpnd_id(String spnd_id) {
        this.spnd_id = spnd_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getSpnd_name() {
        return spnd_name;
    }

    public void setSpnd_name(String spnd_name) {
        this.spnd_name = spnd_name;
    }

    public String getSpnd_prod() {
        return spnd_prod;
    }

    public void setSpnd_prod(String spnd_prod) {
        this.spnd_prod = spnd_prod;
    }

    public String getSpnd_enddate() {
        return spnd_enddate;
    }

    public void setSpnd_enddate(String spnd_enddate) {
        this.spnd_enddate = spnd_enddate;
    }

    public Integer getSpnd_amt() {
        return spnd_amt;
    }

    public void setSpnd_amt(Integer spnd_amt) {
        this.spnd_amt = spnd_amt;
    }

    public byte[] getSpnd_img() {
        return spnd_img;
    }

    public void setSpnd_img(byte[] spnd_img) {
        this.spnd_img = spnd_img;
    }

}