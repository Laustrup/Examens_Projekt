package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Project;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectCreatorTest {

    private ProjectCreator projectCreator = new ProjectCreator();

    @ParameterizedTest
    @CsvSource(value = {"Save the homeless|Andy boss"}, delimiter = '|')
    public void createProjectTest(String title, String managerName) {
        //Act
        Project actual = projectCreator.createProject(title,managerName);

        //Assert
        assertEquals(actual.getTitle(),title);
        assertEquals(actual.getProjectManager().getUsername(),managerName);
    }

    // Only tests title since the method getProject is being fully tested through
    // TODO Perhaps add more parameters to test multiple tests
    @ParameterizedTest
    @CsvSource(value = {"lone9242|1|Appdev"}, delimiter = '|')
    public void getProjectsTest(String userId, String amounts, String expected) {
        //Act
        ArrayList<Project> actual = projectCreator.getProjects(userId);

        //Assert
        for (int i = 0; i < Integer.parseInt(amounts);i++) {
            assertEquals(actual.get(0).getTitle(),expected);
        }
    }

}