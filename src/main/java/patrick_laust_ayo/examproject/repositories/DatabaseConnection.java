package patrick_laust_ayo.examproject.repositories;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DatabaseConnection {

    private Properties properties;
    private Connection connection;

    private String dbConnection;
    private String username;
    private String password;


    public Connection getConnection() {

        // Reads from properties for the purpose of hidding password
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")){
            properties = new Properties();
            properties.load(stream);
            dbConnection = properties.getProperty("connection");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            connection = DriverManager.getConnection(dbConnection,username,password);
        }
        catch(Exception e){
            System.out.println("Problem with property file reading...\n" + e.getMessage());
            e.printStackTrace();
            connection = null;
        }
        return connection;
    }
}
