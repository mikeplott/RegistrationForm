package com.company;

import java.util.ArrayList;

/**
 * Created by michaelplott on 10/17/16.
 */
public class UsersWrapper {
    ArrayList<User> users;

    public UsersWrapper() {
    }

    public UsersWrapper(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "UsersWrapper{" +
                "users=" + users +
                '}';
    }
}
