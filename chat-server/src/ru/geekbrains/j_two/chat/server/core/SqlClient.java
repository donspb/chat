package ru.geekbrains.j_two.chat.server.core;

import org.sqlite.JDBC;

import java.sql.*;

public class SqlClient {

    private static Connection connection;
    private static Statement statement;

    private static PreparedStatement updateStatement;
    private static PreparedStatement getNickStatement;


    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC.PREFIX + "chat-server/clients.db");
            statement = connection.createStatement();

            updateStatement = connection.prepareStatement("UPDATE clients SET nickname = ? WHERE nickname = ?");
            getNickStatement = connection.prepareStatement("SELECT nickname FROM clients WHERE login = ? and password = ?");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNickname(String login, String password) throws SQLException {
        //String query = String.format("select nickname from clients where login='%s' and password='%s'", login, password);
        getNickStatement.setString(1, login);
        getNickStatement.setString(2, password);
        try  (ResultSet set = getNickStatement.executeQuery()) {
            if (set.next()) {
                return set.getString("nickname");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean changeNickname(String newNickname, String oldNickname) throws SQLException {
        updateStatement.setString(1, newNickname);
        updateStatement.setString(2, oldNickname);
        if (updateStatement.executeUpdate() > 0) return true;
//        String query = String.format("UPDATE clients SET nickname = '%s' WHERE nickname = '%s'", newNickname, oldNickname);
//        if (statement.executeUpdate(query) > 0) return true;
        else return false;
    }

    public static void disconnect() {
        try {
            updateStatement.close();
            getNickStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}