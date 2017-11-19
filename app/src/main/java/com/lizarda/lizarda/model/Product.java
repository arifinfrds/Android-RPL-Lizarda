package com.lizarda.lizarda.model;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class Product {

    private String nama;
    private String detail;

    public Product(String nama, String detail) {
        this.nama = nama;
        this.detail = detail;
    }

    public String getNama() {
        return nama;
    }

    public String getDetail() {
        return detail;
    }
}
