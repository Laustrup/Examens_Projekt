package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatorTest {

    private UserCreator userCreator = new UserCreator();
    private ProjectRepository projectRepo = new ProjectRepository();

    @Test
    public void createParticipantTest() {
        //Arrange
        Participant participant;
        int expectedId = projectRepo.calcNextId("participant");
        int actualId = -1;

        //Act
        userCreator.createParticipant("Appdev", "COPENHAGEN");
        actualId = projectRepo.calcNextId("participant") - 1;

        //Assert
        assertEquals(expectedId,actualId);
    }
    @ParameterizedTest
    @CsvSource(value = {"|"}, delimiter = '|')
    public void getProjectManagerTest(String username){

        //Arrange
        ProjectManager projectManager;

        //Act
        projectManager = userCreator.getProjectManager("Andy boss");

        //Assert
        assertEquals(2, projectManager.getDepartment().getDepartmentNo());
        assertEquals("COPENHAGEN", projectManager.getDepartment().getDepName());


    }

}