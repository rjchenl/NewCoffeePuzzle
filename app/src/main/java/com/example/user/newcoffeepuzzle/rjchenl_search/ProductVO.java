package com.example.user.newcoffeepuzzle.rjchenl_search;

import java.io.Serializable;

public class ProductVO implements Serializable {

	private String prod_id;
	private String store_id;
	private String prod_name;
	private String cate_id;
	private Integer prod_price;
	private Integer prod_category;
	private byte[] prod_img;
	private Integer prod_amt;
	private Integer prod_launch;

	private String store_name;

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public ProductVO(){}

    public ProductVO(String prod_name, Integer prod_price,String prod_id) {
        this.prod_name = prod_name;
        this.prod_price = prod_price;
		this.prod_id = prod_id;
    }

    public ProductVO(String prod_id, String store_id, String prod_name, String cate_id, Integer prod_price,
                     Integer prod_category, byte[] prod_img, Integer prod_amt, Integer prod_launch, String store_name) {
		super();
		this.prod_id = prod_id;
		this.store_id = store_id;
		this.prod_name = prod_name;
		this.cate_id = cate_id;
		this.prod_price = prod_price;
		this.prod_category = prod_category;
		this.prod_img = prod_img;
		this.prod_amt = prod_amt;
		this.prod_launch = prod_launch;
		this.store_name = store_name;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getCate_id() {
		return cate_id;
	}

	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}

	public Integer getProd_price() {
		return prod_price;
	}

	public void setProd_price(Integer prod_price) {
		this.prod_price = prod_price;
	}

	public Integer getProd_category() {
		return prod_category;
	}

	public void setProd_category(Integer prod_category) {
		this.prod_category = prod_category;
	}

	public byte[] getProd_img() {
		return prod_img;
	}

	public void setProd_img(byte[] prod_img) {
		this.prod_img = prod_img;
	}

	public Integer getProd_amt() {
		return prod_amt;
	}

	public void setProd_amt(Integer prod_amt) {
		this.prod_amt = prod_amt;
	}

	public Integer getProd_launch() {
		return prod_launch;
	}

	public void setProd_launch(Integer prod_launch) {
		this.prod_launch = prod_launch;
	}

}