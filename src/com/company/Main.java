package com.company;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (results.next()) {
            Integer id = results.getInt("id");
            String name = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, name, address, email));
        }
        return users;
    }

    public static User selectOneUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            Integer uID = results.getInt("id");
            String name = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            return new User(uID, name, address, email);
        }
        return null;
    }

    public static void updateUser(Connection conn, String name, String address, String email, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ? WHERE id = ?");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.setInt(4, id);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./registration");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                (request, response) -> {
                    JsonSerializer serializer = new JsonSerializer();
                    ArrayList<User> users = selectUsers(conn);
                    return serializer.deep(true).serialize(users);
                }
        );

        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    HashMap<String, String> user = parser.parse(body);
                    insertUser(conn, user.get("username"), user.get("address"), user.get("email"));
                    return "";
                }
        );

        Spark.put(
                "/user",
                (request, response) -> {
                    JsonParser parser = new JsonParser();
                    User user = parser.parse(request.body(), User.class);
                    updateUser(conn, user.username, user.address, user.email, user.id);
                    return "";
                }
        );

        Spark.delete(
                "/user/:id",
                (request, response) -> {
                    JsonParser parser = new JsonParser();
                    Integer id = parser.parse(request.params(":id"));
                    deleteUser(conn, id);
                    return "";
                }
        );
    }
}
