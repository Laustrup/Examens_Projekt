package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectmanagerRepository {

    private ProjectManager projectmanager;
    private DatabaseConnection databaseConnection;

    public ProjectManager findProjectManagerFromUsername(String username) {
        Connection connection = databaseConnection.getConnection();
        ResultSet res = executeQuery(connection, "SELECT * FROM PROJECTMANAGER WHERE Username = "
                                    + username + ";");

        try {
            projectmanager = new ProjectManager(res.getString(1),res.getString(2),);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            projectmanager = null;
        }

        return projectmanager;
    }

    private ResultSet executeQuery(Connection connection, String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        }
        catch (Exception e) {
            System.out.println("Couldn't execute query...\n" + e.getMessage());
            return null;
        }
    }

}
