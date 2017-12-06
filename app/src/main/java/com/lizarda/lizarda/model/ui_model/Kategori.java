package com.lizarda.lizarda.model.ui_model;

import com.lizarda.lizarda.R;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class Kategori {

    private int imageResId;
    private String nama;

    public Kategori(int imageResId, String nama) {
        this.imageResId = imageResId;
        this.nama = nama;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getNama() {
        return nama;
    }

    public static ArrayList<Kategori> getCategories() {
        ArrayList<Kategori> categories = new ArrayList<>();
        categories.add(new Kategori(R.drawable.ic_kategori_chameleon, "Bunglon"));
        categories.add(new Kategori(R.drawable.ic_kategori_snake, "Ular"));
        categories.add(new Kategori(R.drawable.ic_kategori_iguana, "Iguana"));
        categories.add(new Kategori(R.drawable.kategori_kura_kura, "Kura-kura"));
        return categories;
    }
}
