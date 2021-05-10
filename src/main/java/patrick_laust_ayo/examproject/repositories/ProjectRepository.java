package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectRepository extends Repository{

    private Project project;
    private Phase phase;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        Connection connection = databaseConnection.getConnection();
        executeSQLStatement(connection,"insert into project values (default, \""  + projectToInsert.getTitle() + "\", \"" +
                projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
    }

    public Project putProjectInDatabaseWithReturn(Project projectToInsert, int projectmanagerId, ProjectManager projectManager) {
        Connection connection = databaseConnection.getConnection();
        executeSQLStatement(connection,"insert into project values (default, \""  + projectToInsert.getTitle() + "\", \"" +
                                            projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
        ResultSet res = executeQuery(connection,"SELECT * FROM project WHERE title = \"" + projectToInsert.getTitle() + "\";");

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

}
