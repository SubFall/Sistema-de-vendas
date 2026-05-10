package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/eclipse_net";
        String username = "app_user";
        String password = "app123";

        return DriverManager.getConnection(url, username, password);
    }
}
