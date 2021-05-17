package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectManagerRepository extends Repository {

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
        closeCurrentConnection();

        try{
            projectmanager = new ProjectManager(res.getString("username"),
                            res.getString("projectmanager_password"));
        }
        catch(SQLException e){
            System.out.println("Couldn't put projectmanager in database " + e.getMessage());
        }

        return projectmanager;
    }

    public void updateProjectManager(ProjectManager projectmanager, String newUsername, String newPassword, String formerUsername) {

        executeSQLStatement("UPDATE projectmanager " +
                "SET projectmanager.username = '" + newUsername + "' " +
                "WHERE projectmanager.username = '" + formerUsername + "';");
        executeSQLStatement("UPDATE participant " +
                "SET participant.participant_password = '" + newPassword + "' " +
                "WHERE participant.position = 'Manager';");
    }

    public ProjectManager findProjectManager(String username) {
        ResultSet res = executeQuery("SELECT projectmanager_id, username, " +
                "participant_password, " + "participant.user_id, " + "participant_name, " +
                "position, project_id, department.department_no, location, department_name " +
                "FROM projectmanager " +
                "INNER JOIN participant " +
                "INNER JOIN department " +
                "WHERE projectmanager.username = '" + username + "' " +
                "and participant.department_no = department.department_no;");
        closeCurrentConnection();

        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                                    res.getString("location"), res.getString("department_name"));
            projectmanager = new ProjectManager(res.getString("username"),res.getString("participant_password"),
                                                res.getString("user_id"),res.getString("participant_name"),
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
