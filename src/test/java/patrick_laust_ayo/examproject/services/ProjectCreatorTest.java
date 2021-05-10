package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.Repository;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ProjectCreatorTest {

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectRepository repo = new ProjectRepository();

    @ParameterizedTest
    @CsvSource(value = {"Save the homeless|dafsgf41*ø|Andy Boss|Save the homeless_dafsgf41*ø_Andy Boss"}, delimiter = '|')
    public void createProjectTest(String title, String password, String managerName, String expected) {
        //Arrange
        String[] expectations = expected.split("_");

        //Act
        Project project = projectCreator.createProject(title,password,managerName);

        //Assert
        assertEquals(project.getTitle(),expectations[0]);
        assertEquals(project.getPassword(),expectations[1]);
        assertEquals(project.getProjectManager().getName(),expectations[2]);

    }

}