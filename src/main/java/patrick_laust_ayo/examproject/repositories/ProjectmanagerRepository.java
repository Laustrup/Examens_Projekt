package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectmanagerRepository extends Repository {

    private ProjectManager projectmanager;

    // -1 for if value is unchanged
    private int currentId = -1;

    public void putProjectManagerInDatabase(ProjectManager projectManager){
        executeSQLStatement("INSERT INTO projectmanager VALUES (default, \"" + projectManager.getUsername()
                            + "\", \"" + projectManager.getPassword() + "\", default");
    }


    public ProjectManager putProjectManagerInDatabaseWithReturn(ProjectManager projectManager){
        executeSQLStatement("INSERT INTO projectmanager VALUES (default, \"" + projectManager.getUsername()
                + "\", \"" + projectManager.getPassword() + "\", default");
        ResultSet res = executeQuery("SELECT * FROM projectmanager");

        try{
            projectmanager = new ProjectManager(res.getString("username"),
                            res.getString("projectmanager_password"));
        }
        catch(SQLException e){
            System.out.println("Couldn't put projectmanager in database " + e.getMessage());
        }

        return projectmanager;
    }


    public ProjectManager findProjectManagerFromUsername(String username) {
        ResultSet res = executeQuery("SELECT projectmanager_id, username, " +
                "projectmanager_password, " + "participant.participant_id, " + "participant_name, " +
                "position, project_id, department.department_no, location, department_name " +
                "FROM projectmanager " +
                "INNER JOIN participant ON participant.participant_id = projectmanager.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.participant_id " +
                "WHERE projectmanager.username = '" + username + "';");

        try {
            Department department = new Department(res.getString("location"),
                                    res.getString("department_name"), res.getInt("department_no"));
            projectmanager = new ProjectManager(res.getString("username"),res.getString("projectmanager_password"),
                                                res.getInt("participant_id"),res.getString("participant_name"),
                                                res.getString("position"),department);

            currentId = res.getInt("projectmanager_id");
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            projectmanager = null;
        }

        return projectmanager;
    }

    public int getCurrentId() {
            return currentId;
    }

}
