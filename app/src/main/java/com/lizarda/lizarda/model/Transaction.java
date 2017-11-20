package com.lizarda.lizarda.model;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class Transaction {

    private String id;
    private ArrayList<String> productsId;
    private double total;

    public Transaction() {
    }

    public Transaction(String id, ArrayList<String> productsId, double total) {
        this.id = id;
        this.productsId = productsId;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(ArrayList<String> productsId) {
        this.productsId = productsId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
