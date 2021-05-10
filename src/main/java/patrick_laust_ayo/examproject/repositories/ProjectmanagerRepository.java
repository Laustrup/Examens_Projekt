package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectmanagerRepository {

    private ProjectManager projectmanager;
    private DatabaseConnection databaseConnection;

    public ProjectManager findProjectManagerFromUsername(String username) {
        Connection connection = databaseConnection.getConnection();
        ResultSet res = executeQuery(connection, "SELECT * FROM projectmanager WHERE username = " + username +
                                        " INNER JOIN projectmanager.projectmanager_id = participant.participant_id," +
                                        " INNER JOIN department.department_no = participant.participant_id;");

        try {
            Department department = new Department(res.getString("location"), res.getString("department_name"),
                                                    res.getInt("department_no"));
            projectmanager = new ProjectManager(res.getString("username"),res.getString("projectmanager_password"),
                                                res.getInt("participant_id"),res.getString("participant_name"),
                                                res.getString("position"),department);

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
