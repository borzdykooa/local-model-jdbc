package com.borzdykooa.connection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/*
Класс, отвечающий за соединение с базой данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Connector {

    public static Connection getConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("application");
        String url = resource.getString("db.url");
        String user = resource.getString("db.user");
        String pass = resource.getString("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
