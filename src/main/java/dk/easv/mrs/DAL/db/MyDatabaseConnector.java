package dk.easv.mrs.DAL.db;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MyDatabaseConnector {
    private static final String PROP_FILE = "config/database.settings";
    private SQLServerDataSource dataSource;

    public MyDatabaseConnector() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(Files.newInputStream(Paths.get(PROP_FILE)));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName("Server");
        dataSource.setDatabaseName("Database");
        dataSource.setUser("User");
        dataSource.setPassword("Password");
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws SQLException, IOException {
        MyDatabaseConnector databaseConnector = new MyDatabaseConnector();
       try (Connection connection = databaseConnector.getConnection()){
           System.out.println("is it open? " + !connection.isClosed());
       }

    }
}
