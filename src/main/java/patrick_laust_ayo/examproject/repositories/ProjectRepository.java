package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectRepository {

    private Project project;
    private Phase phase;

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
