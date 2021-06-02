package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.ResultSet;

//Author Laust
public class ProjectManagerRepository extends Repository {

    private ParticipantRepository parRepo = new ParticipantRepository();

    public void putProjectManagerInDatabase(ProjectManager projectManager){
        try {
            executeSQLStatement("INSERT INTO projectmanager(username,participant_id) VALUES ('" + projectManager.getUsername()
                    + "', " + parRepo.findParticipant(projectManager.getId()).findColumn("participant_id") + ");");
        }
        catch (Exception e) {
            System.out.println("Couldn't put projectmanager in database..\n" + e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateProjectManager(String newUsername, String formerUsername) {
        executeSQLStatement("UPDATE projectmanager " +
                "SET projectmanager.username = '" + newUsername + "' " +
                "WHERE projectmanager.username = '" + formerUsername + "';");
    }

    public ResultSet findProjectManager(String username) {
        return executeQuery("SELECT * " +
                "FROM projectmanager " +
                "WHERE projectmanager.username = \"" + username + "\";");
    }

    public void removeProjectManager(String username) {
        executeSQLStatement("DELETE ROW FROM projectmanager WHERE username = " + username);

    }
}
