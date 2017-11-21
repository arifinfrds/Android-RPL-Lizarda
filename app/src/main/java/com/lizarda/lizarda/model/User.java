package com.lizarda.lizarda.model;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class User {

    private String id;
    private String email;
    private String nama;
    private String photoUrl;
    private boolean isAdmin;
    private double saldo;

    public User() {
    }

    public User(String id, String email, String nama, String photoUrl, boolean isAdmin, double saldo) {
        this.id = id;
        this.email = email;
        this.nama = nama;
        this.photoUrl = photoUrl;
        this.isAdmin = isAdmin;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
