package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory implements ConnectionProvider {
    private static final String URL = "jdbc:mysql://localhost:3306/eclipse_net";
    private static final String USERNAME = "app_user";
    private static final String PASSWORD = "app123";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
