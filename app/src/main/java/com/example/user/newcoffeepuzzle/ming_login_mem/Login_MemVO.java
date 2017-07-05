package com.example.user.newcoffeepuzzle.ming_login_mem;

/**
 * Created by Java on 2017/7/3.
 */

public class Login_MemVO {
    private String mem_id;
    private String mem_acct;
    private String mem_pwd;
    private String mem_name;
    private String mem_tel;
    private String mem_email;
    private String mem_add;
    private Integer mem_points;
    private byte[] mem_img;

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_acct() {
        return mem_acct;
    }

    public void setMem_acct(String mem_acct) {
        this.mem_acct = mem_acct;
    }

    public String getMem_pwd() {
        return mem_pwd;
    }

    public void setMem_pwd(String mem_pwd) {
        this.mem_pwd = mem_pwd;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_tel() {
        return mem_tel;
    }

    public void setMem_tel(String mem_tel) {
        this.mem_tel = mem_tel;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_add() {
        return mem_add;
    }

    public void setMem_add(String mem_add) {
        this.mem_add = mem_add;
    }

    public Integer getMem_points() {
        return mem_points;
    }

    public void setMem_points(Integer mem_points) {
        this.mem_points = mem_points;
    }

    public byte[] getMem_img() {
        return mem_img;
    }

    public void setMem_img(byte[] mem_img) {
        this.mem_img = mem_img;
    }

    public Login_MemVO(String mem_id, String mem_acct, String mem_pwd, String mem_name, String mem_tel, String mem_email, String mem_add, Integer mem_points, byte[] mem_img) {

        this.mem_id = mem_id;
        this.mem_acct = mem_acct;
        this.mem_pwd = mem_pwd;
        this.mem_name = mem_name;
        this.mem_tel = mem_tel;
        this.mem_email = mem_email;
        this.mem_add = mem_add;
        this.mem_points = mem_points;
        this.mem_img = mem_img;
    }
}
