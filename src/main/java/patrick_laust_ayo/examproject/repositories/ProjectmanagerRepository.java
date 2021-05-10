package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;
import java.sql.ResultSet;

public class ProjectmanagerRepository extends Repository {

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

}
