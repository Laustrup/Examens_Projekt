package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;

import java.sql.Connection;

public class ProjectRepository extends Repository{

    private Project project;
    private Phase phase;
    private DatabaseConnection databaseConnection;

    public Project putProjectInDatabase(Project projectToInsert) {
        Connection connection = databaseConnection.getConnection();
        executeSQLStatement(connection,"insert into project values (default, " +
                                            projectToInsert.getPassword() + ", \"1\"); ");

        try {
            Department department = new Department(res.getString("location"), res.getString("department_name"),
                    res.getInt("department_no"));
            projectmanager = new ProjectManager(),
                    res.getString("position"),department);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            projectmanager = null;
        }

        return project;
    }

}
