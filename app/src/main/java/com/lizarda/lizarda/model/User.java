package com.lizarda.lizarda.model;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class User {

    private String id;
    private String email;
    private String photoUrl;
    private String idAdmin;
    private double saldo;

    public User() {
    }

    public User(String id, String email, String photoUrl, String idAdmin, double saldo) {
        this.id = id;
        this.email = email;
        this.photoUrl = photoUrl;
        this.idAdmin = idAdmin;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
