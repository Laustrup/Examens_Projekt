package patrick_laust_ayo.examproject.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Repository {

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    // Methods made to perform try and catch and as well to be used multiple times
    public ResultSet executeQuery(String sql) {
        Connection connection = databaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
            return null;
        }
    }

    protected void executeSQLStatement(String sql) {
        Connection connection = databaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
        }
    }

    public int calcNextId(String table) {
        ResultSet res = executeQuery("SELECT * FROM " + table + ";");

        int nextId = 0;

        try {
            while (res.next()) {
                nextId++;
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong with calculating next id...\n" + e.getMessage());
        }

        return nextId + 1;
    }
}
