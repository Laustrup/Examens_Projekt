package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectRepository extends Repository{

    private Project project;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project VALUES (DEFAULT, \""  + projectToInsert.getTitle() + "\", \"" +
                projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
    }

    public Project putProjectInDatabaseWithReturn(Project projectToInsert, int projectmanagerId, ProjectManager projectManager) {
        executeSQLStatement("insert into project values (default, \""  + projectToInsert.getTitle() + "\", \"" +
                                            projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
        ResultSet res = executeQuery("SELECT * FROM project WHERE title = \"" + projectToInsert.getTitle() + "\";");

        try {
            project = new Project(res.getString("title"), res.getString("project_password"),
                      new ArrayList<Phase>(), new HashMap<>(), projectManager);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            project = null;
        }

        return project;
    }

    public void putPhaseInDatabase(int projectmanagerId) {
        executeSQLStatement("INSERT INTO phase VALUES (default, "  + null + ", " + projectmanagerId + "); ");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId, int participantId, int taskId) {

        executeSQLStatement("INSERT INTO assignment VALUES (default, "  + assignment.getStart() +
                            ", " + assignment.getEnd() + ", " + assignment.isCompleted() + ", " +
                             phaseId + ", " + participantId + ", " + taskId + "); ");
    }


    // TODO
    public Project findProject(String title) {
        return new Project(null,null,new ArrayList<>(),new HashMap<>(),new ProjectManager());
    }

    public boolean doesProjectExist(String title){
        ResultSet res = executeQuery("SELECT * FROM project WHERE title = " + title + ";");

        try {
            while (res.next()) {
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't look through resultSet...\n" + e.getMessage());
        }

        return false;

    }

}
