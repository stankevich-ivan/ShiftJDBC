package org.shift.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.username";
    private static final String DB_PASSWORD_KEY = "db.password";

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperty(DB_URL_KEY),
                    PropertiesUtil.getProperty(DB_USER_KEY),
                    PropertiesUtil.getProperty(DB_PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
