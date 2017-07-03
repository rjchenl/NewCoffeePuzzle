package com.example.user.newcoffeepuzzle.rjchenl_search;

import java.io.Serializable;
import java.sql.Timestamp;

public class StoreVO implements Serializable {

    private String store_id;
    private String store_acct;
    private String store_pwd;
    private String store_name;
    private String store_tel;
    private String store_add;
    private String store_email;
    private Double longitude;
    private Double latitude;
    private Integer store_points;
    private String store_cpse;
    private Integer min_order;
    private Integer is_min_order;
    private Integer is_wifi;
    private Integer is_plug;
    private Integer is_single_orgn;
    private Integer is_dessert;
    private Integer is_meal;
    private Integer is_time_limit;
    private Integer mon_isopen;
    private Timestamp mon_open;
    private Timestamp mon_close;
    private Integer tue_isopen;
    private Timestamp tue_open;
    private Timestamp tue_close;
    private Integer wed_isopen;
    private Timestamp wed_open;
    private Timestamp wed_close;
    private Integer thu_isopen;
    private Timestamp thu_open;
    private Timestamp thu_close;
    private Integer fri_isopen;
    private Timestamp fri_open;
    private Timestamp fri_close;
    private Integer sat_isopen;
    private Timestamp sat_open;
    private Timestamp sat_close;
    private Integer sun_isopen;
    private Timestamp sun_open;
    private Timestamp sun_close;
    private byte[] store_img;
    private Integer store_pass;


    public StoreVO(){}

    public StoreVO(String store_id, String store_acct, String store_pwd, String store_name, String store_tel, String store_add, String store_email, Double longitude, Double latitude, Integer store_points, String store_cpse, Integer min_order, Integer is_min_order, Integer is_wifi, Integer is_plug, Integer is_single_orgn, Integer is_dessert, Integer is_meal, Integer is_time_limit, Integer mon_isopen, Timestamp mon_open, Timestamp mon_close, Integer tue_isopen, Timestamp tue_open, Timestamp tue_close, Integer wed_isopen, Timestamp wed_open, Timestamp wed_close, Integer thu_isopen, Timestamp thu_open, Timestamp thu_close, Integer fri_isopen, Timestamp fri_open, Timestamp fri_close, Integer sat_isopen, Timestamp sat_open, Timestamp sat_close, Integer sun_isopen, Timestamp sun_open, Timestamp sun_close, byte[] store_img, Integer store_pass){
        this.store_id = store_id;
        this.store_acct = store_acct;
        this.store_pwd = store_pwd;
        this.store_name = store_name;
        this.store_tel = store_tel;
        this.store_add = store_add;
        this.store_email = store_email;
        this.longitude = longitude;
        this.latitude = latitude;
        this.store_points = store_points;
        this.store_cpse = store_cpse;
        this.min_order = min_order;
        this.is_min_order = is_min_order;
        this.is_wifi = is_wifi;
        this.is_plug = is_plug;
        this.is_single_orgn = is_single_orgn;
        this.is_dessert = is_dessert;
        this.is_meal = is_meal;
        this.is_time_limit = is_time_limit;
        this.mon_isopen = mon_isopen;
        this.mon_open = mon_open;
        this.mon_close = mon_close;
        this.tue_isopen = tue_isopen;
        this.tue_open = tue_open;
        this.tue_close = tue_close;
        this.wed_isopen = wed_isopen;
        this.wed_open = wed_open;
        this.wed_close = wed_close;
        this.thu_isopen = thu_isopen;
        this.thu_open = thu_open;
        this.thu_close = thu_close;
        this.fri_isopen = fri_isopen;
        this.fri_open = fri_open;
        this.fri_close = fri_close;
        this.sat_isopen = sat_isopen;
        this.sat_open = sat_open;
        this.sat_close = sat_close;
        this.sun_isopen = sun_isopen;
        this.sun_open = sun_open;
        this.sun_close = sun_close;
        this.store_img = store_img;
        this.store_pass = store_pass;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_acct() {
        return store_acct;
    }

    public void setStore_acct(String store_acct) {
        this.store_acct = store_acct;
    }

    public String getStore_pwd() {
        return store_pwd;
    }

    public void setStore_pwd(String store_pwd) {
        this.store_pwd = store_pwd;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getStore_add() {
        return store_add;
    }

    public void setStore_add(String store_add) {
        this.store_add = store_add;
    }

    public String getStore_email() {
        return store_email;
    }

    public void setStore_email(String store_email) {
        this.store_email = store_email;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getStore_points() {
        return store_points;
    }

    public void setStore_points(Integer store_points) {
        this.store_points = store_points;
    }

    public String getStore_cpse() {
        return store_cpse;
    }

    public void setStore_cpse(String store_cpse) {
        this.store_cpse = store_cpse;
    }

    public Integer getMin_order() {
        return min_order;
    }

    public void setMin_order(Integer min_order) {
        this.min_order = min_order;
    }

    public Integer getIs_min_order() {
        return is_min_order;
    }

    public void setIs_min_order(Integer is_min_order) {
        this.is_min_order = is_min_order;
    }

    public Integer getIs_wifi() {
        return is_wifi;
    }

    public void setIs_wifi(Integer is_wifi) {
        this.is_wifi = is_wifi;
    }

    public Integer getIs_plug() {
        return is_plug;
    }

    public void setIs_plug(Integer is_plug) {
        this.is_plug = is_plug;
    }

    public Integer getIs_single_orgn() {
        return is_single_orgn;
    }

    public void setIs_single_orgn(Integer is_single_orgn) {
        this.is_single_orgn = is_single_orgn;
    }

    public Integer getIs_dessert() {
        return is_dessert;
    }

    public void setIs_dessert(Integer is_dessert) {
        this.is_dessert = is_dessert;
    }

    public Integer getIs_meal() {
        return is_meal;
    }

    public void setIs_meal(Integer is_meal) {
        this.is_meal = is_meal;
    }

    public Integer getIs_time_limit() {
        return is_time_limit;
    }

    public void setIs_time_limit(Integer is_time_limit) {
        this.is_time_limit = is_time_limit;
    }

    public Integer getMon_isopen() {
        return mon_isopen;
    }

    public void setMon_isopen(Integer mon_isopen) {
        this.mon_isopen = mon_isopen;
    }

    public Timestamp getMon_open() {
        return mon_open;
    }

    public void setMon_open(Timestamp mon_open) {
        this.mon_open = mon_open;
    }

    public Timestamp getMon_close() {
        return mon_close;
    }

    public void setMon_close(Timestamp mon_close) {
        this.mon_close = mon_close;
    }

    public Integer getTue_isopen() {
        return tue_isopen;
    }

    public void setTue_isopen(Integer tue_isopen) {
        this.tue_isopen = tue_isopen;
    }

    public Timestamp getTue_open() {
        return tue_open;
    }

    public void setTue_open(Timestamp tue_open) {
        this.tue_open = tue_open;
    }

    public Timestamp getTue_close() {
        return tue_close;
    }

    public void setTue_close(Timestamp tue_close) {
        this.tue_close = tue_close;
    }

    public Integer getWed_isopen() {
        return wed_isopen;
    }

    public void setWed_isopen(Integer wed_isopen) {
        this.wed_isopen = wed_isopen;
    }

    public Timestamp getWed_open() {
        return wed_open;
    }

    public void setWed_open(Timestamp wed_open) {
        this.wed_open = wed_open;
    }

    public Timestamp getWed_close() {
        return wed_close;
    }

    public void setWed_close(Timestamp wed_close) {
        this.wed_close = wed_close;
    }

    public Integer getThu_isopen() {
        return thu_isopen;
    }

    public void setThu_isopen(Integer thu_isopen) {
        this.thu_isopen = thu_isopen;
    }

    public Timestamp getThu_open() {
        return thu_open;
    }

    public void setThu_open(Timestamp thu_open) {
        this.thu_open = thu_open;
    }

    public Timestamp getThu_close() {
        return thu_close;
    }

    public void setThu_close(Timestamp thu_close) {
        this.thu_close = thu_close;
    }

    public Integer getFri_isopen() {
        return fri_isopen;
    }

    public void setFri_isopen(Integer fri_isopen) {
        this.fri_isopen = fri_isopen;
    }

    public Timestamp getFri_open() {
        return fri_open;
    }

    public void setFri_open(Timestamp fri_open) {
        this.fri_open = fri_open;
    }

    public Timestamp getFri_close() {
        return fri_close;
    }

    public void setFri_close(Timestamp fri_close) {
        this.fri_close = fri_close;
    }

    public Integer getSat_isopen() {
        return sat_isopen;
    }

    public void setSat_isopen(Integer sat_isopen) {
        this.sat_isopen = sat_isopen;
    }

    public Timestamp getSat_open() {
        return sat_open;
    }

    public void setSat_open(Timestamp sat_open) {
        this.sat_open = sat_open;
    }

    public Timestamp getSat_close() {
        return sat_close;
    }

    public void setSat_close(Timestamp sat_close) {
        this.sat_close = sat_close;
    }

    public Integer getSun_isopen() {
        return sun_isopen;
    }

    public void setSun_isopen(Integer sun_isopen) {
        this.sun_isopen = sun_isopen;
    }

    public Timestamp getSun_open() {
        return sun_open;
    }

    public void setSun_open(Timestamp sun_open) {
        this.sun_open = sun_open;
    }

    public Timestamp getSun_close() {
        return sun_close;
    }

    public void setSun_close(Timestamp sun_close) {
        this.sun_close = sun_close;
    }

    public byte[] getStore_img() {
        return store_img;
    }

    public void setStore_img(byte[] store_img) {
        this.store_img = store_img;
    }

    public Integer getStore_pass() {
        return store_pass;
    }

    public void setStore_pass(Integer store_pass) {
        this.store_pass = store_pass;
    }

}
