package com.example.tanakorn.register;

/**
 * Created by tanakorn on 5/5/2016 AD.
 */
public class User {
    private String name;
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}