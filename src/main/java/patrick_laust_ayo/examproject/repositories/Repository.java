package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Repository {

    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private Connection connection;

    // Methods made to perform try and catch and as well to be used multiple times
    public ResultSet executeQuery(String sql) {
        connection = databaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //closeConnection();
            return statement.executeQuery();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
            closeConnection();
            return null;
        }
    }

    protected void executeSQLStatement(String sql) {
        connection = databaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
        }
        closeConnection();
    }

    private void closeConnection() {
        try {
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Couldn't close connection...\n" + e.getMessage());
        }
    }

    public int findForeignId(String table, String column, String currentKey, String foreignKey) {

        ResultSet res = executeQuery("SELECT * FROM " + table + " WHERE " + column + " = '" + currentKey + "';");

        try {
            return res.getInt(foreignKey);
        }
        catch (Exception e) {
            System.out.println("Couldn't find project id...\n" + e.getMessage());
            return -1;
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
