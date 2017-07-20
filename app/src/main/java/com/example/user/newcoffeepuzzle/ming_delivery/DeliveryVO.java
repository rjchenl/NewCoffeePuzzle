package com.example.user.newcoffeepuzzle.ming_delivery;

/**
 * Created by Java on 2017/7/20.
 */

public class DeliveryVO {
    private String ord_id;
    private String prod_id;
    private String prod_name;
    private Integer prod_price;
    private Integer detail_amt;

    public String getOrd_id() {
        return ord_id;
    }

    public void setOrd_id(String ord_id) {
        this.ord_id = ord_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public Integer getProd_price() {
        return prod_price;
    }

    public void setProd_price(Integer prod_price) {
        this.prod_price = prod_price;
    }

    public Integer getDetail_amt() {
        return detail_amt;
    }

    public void setDetail_amt(Integer detail_amt) {
        this.detail_amt = detail_amt;
    }

    public DeliveryVO(String ord_id, String prod_id, String prod_name, Integer prod_price, Integer detail_amt) {

        this.ord_id = ord_id;
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
        this.detail_amt = detail_amt;
    }
}
