package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.services.UserCreator;

import java.sql.ResultSet;

public class ProjectManagerRepository extends Repository {

    private ParticipantRepository parRepo = new ParticipantRepository();


    public void putProjectManagerInDatabase(ProjectManager projectManager){
        try {
            System.out.println(parRepo.findParticipant(projectManager.getId()).findColumn("participant_id"));
            System.out.println(projectManager.getUsername());
            System.out.println(projectManager.getPassword());
            System.out.println(projectManager.getDepartment());
            System.out.println(projectManager.getPosition());
            System.out.println(projectManager.getName());
        }
        catch(Exception e){
            System.out.println("For fanden");
        }
        try {
            executeSQLStatement("INSERT INTO projectmanager(username,participant_id) VALUES ('" + projectManager.getUsername()
                    + "', " + parRepo.findParticipant(projectManager.getId()).findColumn("participant_id") + ");");
        }
        catch (Exception e) {
            System.out.println("Couldn't put projectmanager in database..\n" + e.getMessage());
            e.printStackTrace();
        }

    }

/*
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
        closeCurrentConnection();
        return projectmanager;
    }

 */

    public void updateProjectManager(String newUsername, String newPassword, String formerUsername) {
        executeSQLStatement("UPDATE projectmanager " +
                "SET projectmanager.username = '" + newUsername + "' " +
                "WHERE projectmanager.username = '" + formerUsername + "';");
        executeSQLStatement("UPDATE participant " +
                "SET participant.participant_password = '" + newPassword + "' " +
                "WHERE participant.position = 'Manager';");
    }

    public ResultSet findProjectManager(String username) {
        return executeQuery("SELECT projectmanager_id, username, " +
                "participant_password, " + "participant.user_id, " + "participant_name, " +
                "position, project_id, department.department_no, location, department_name " +
                "FROM projectmanager " +
                "INNER JOIN participant " +
                "INNER JOIN department " +
                "WHERE projectmanager.username = '" + username + "' " +
                "and participant.department_no = department.department_no;");
    }
}
