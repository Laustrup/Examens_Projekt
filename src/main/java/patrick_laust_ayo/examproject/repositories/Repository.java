package patrick_laust_ayo.examproject.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Repository {

    // Methods made to perform try and catch and as well to be used multiple times
    protected ResultSet executeQuery(Connection connection, String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
            return null;
        }
    }

    protected void executeSQLStatement(Connection connection, String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
        }
    }
}
