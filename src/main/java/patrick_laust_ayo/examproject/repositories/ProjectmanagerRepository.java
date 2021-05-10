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
        ResultSet res = executeQuery(connection, "SELECT projectmanager_id, username, " +
                "projectmanager_password, " + "participant.participant_id, " + "participant_name, " +
                "position, project_id, department.department_no, location, department_name " +
                "FROM projectmanager" +
                "INNER JOIN participant ON participant.participant_id = projectmanager.projectmanager_id" +
                "INNER JOIN department ON department.department_no = participant.participant_id" +
                "WHERE projectmanager.username = '" + username + "';");

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
