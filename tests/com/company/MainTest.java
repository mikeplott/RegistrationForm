package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by michaelplott on 10/17/16.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }

    @Test
    public void testSelectAndInsertUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Mike", "ajlksdfl", "mike@mike.notmike");
        Main.insertUser(conn, "NotMike", "lkajdsflja", "notmike@mike.notmike");
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 2);
    }

    @Test
    public void testUpdateUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Mike", "ajlksdfl", "mike@mike.notmike");
        Main.insertUser(conn, "NotMike", "lkajdsflja", "notmike@mike.notmike");
        Main.updateUser(conn, "Bob", "aldskjfaf", "asjhdflja", 1);
        User user = Main.selectOneUser(conn, 1);
        conn.close();
        assertTrue(user.username.equals("Bob"));
    }

    @Test
    public void testDeleteUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Mike", "ajlksdfl", "mike@mike.notmike");
        Main.insertUser(conn, "NotMike", "lkajdsflja", "notmike@mike.notmike");
        Main.deleteUser(conn, 1);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 1);
    }
}