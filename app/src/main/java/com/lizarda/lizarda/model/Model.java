package com.lizarda.lizarda.model;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 10/31/17.
 */

public class Model {

    private String name;
    private String detail;

    public Model(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public static ArrayList<Model> generateModels() {
        ArrayList<Model> models = new ArrayList<>();
        models.add(new Model("Iguana 1", "Lorem ipsum dolor sit amet"));
        models.add(new Model("Iguana 2", "consectetur adipiscing elit"));
        models.add(new Model("Iguana 3", "Suspendisse sodales ipsum sed lacus tincidunt"));
        models.add(new Model("Iguana 4", "non malesuada sapien consequat"));
        models.add(new Model("Iguana 5", "Aliquam dignissim tortor tincidunt"));
        models.add(new Model("Iguana 6", "sagittis libero et"));
        return models;
    }
}
