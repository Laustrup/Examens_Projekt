package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import patrick_laust_ayo.examproject.models.Project;

import static org.junit.jupiter.api.Assertions.*;

class GetProjectTest {

    @Test
    void getProject() {
        //Arrange
        ProjectCreator pCreator = new ProjectCreator();

        //Act
        Project project = pCreator.getProject("Appdev");

        //Assert
        try {
            assertEquals("Appdev", project.getTitle());
        }
        catch (Exception e){
            System.out.println("Get Project Test Problem " + e.getMessage());
            e.printStackTrace();
        }
    }
}