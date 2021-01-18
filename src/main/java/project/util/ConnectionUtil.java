package project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import project.exception.DataProcessingException;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new DataProcessingException("Can't find MySQL driver", ex);
        }
    }

    public static Connection getConnection() {
        Properties dbProperties = new Properties();
        dbProperties.put("user", "yaroslav");
        dbProperties.put("password", "qwerty");
        String url = "jdbc:mysql://localhost:3306/taxi_service?serverTimezone=UTC";

        try {
            return DriverManager.getConnection(url, dbProperties);
        } catch (SQLException ex) {
            throw new DataProcessingException("can't establish the connection to db", ex);
        }
    }
}
