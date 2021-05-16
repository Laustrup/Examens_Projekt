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

    @ParameterizedTest
    @CsvSource(value = {"Save the homeless|admin1234|Andy Boss"}, delimiter = '|')
    public void createProjectTest(String title, String password, String managerName) {
        //Arrange

        //Act
        Project project = projectCreator.createProject(title,password,managerName);

        //Assert
        assertEquals(project.getTitle(),title);
        assertEquals(project.getPassword(),password);
        assertEquals(project.getProjectManager().getUsername(),managerName);

    }

}