package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USER = "bank_user";
    private static final String PASSWORD = "bank123";

    // Single method to get connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load driver
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null; // return null if connection fails
        }
    }
}
