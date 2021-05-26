package patrick_laust_ayo.examproject.repositories;

import org.junit.jupiter.api.Test;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.services.ProjectCreator;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class CreateProjectTest {

    @Test
    void createProject() {
        //Arrange
        ProjectRepository pRepo = new ProjectRepository();
        ProjectCreator pCreator = new ProjectCreator();

        //Act
        Project project = pCreator.createProject("Cleverbot v.2", "andy0432");
        int projectId = pRepo.findId("project", "title", project.getTitle(), "project_id");
        pRepo.putPhaseInDatabase(projectId);

        //Assert
        try {
            assertEquals("Cleverbot v.2", pCreator.getProject("Cleverbot v.2").getTitle());
        }
        catch (Exception e){
            System.out.println("Find Project Test went wrong " + e.getMessage());
            e.printStackTrace();
        }
    }
}