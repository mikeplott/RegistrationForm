package com.company;

/**
 * Created by michaelplott on 10/17/16.
 */
public class User {
    Integer id;
    String username;
    String address;
    String email;

    public User() {
    }

    public User(String username, String address, String email) {
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public User(Integer id, String username, String address, String email) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
