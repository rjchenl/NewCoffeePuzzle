package com.example.user.newcoffeepuzzle.ming_login_store;

/**
 * Created by Java on 2017/6/28.
 */

public class Login_StoreVO {
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

    public Login_StoreVO(String store_id, String store_acct, String store_pwd, String store_name, String store_tel, String store_add, String store_email) {

        this.store_id = store_id;
        this.store_acct = store_acct;
        this.store_pwd = store_pwd;
        this.store_name = store_name;
        this.store_tel = store_tel;
        this.store_add = store_add;
        this.store_email = store_email;
    }

    private String store_id;
    private String store_acct;
    private String store_pwd;
    private String store_name;
    private String store_tel;
    private String store_add;
    private String store_email;
}
