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
        assertEquals("0152 Oslo Norway - Tollbugata 8 A/B", projectManager.getDepartment().getLocation());
        assertEquals("OSLO", projectManager.getDepartment().getDepName());
        assertEquals("Andy Boss", projectManager.getUsername());
        assertEquals("erAsD14'd", projectManager.getPassword());
        assertEquals("andy0432", projectManager.getId());
        assertEquals("Andy Boss", projectManager.getName());
        assertEquals("Manager", projectManager.getPosition());
    }


    @Test
    void createManager(){
        UserCreator userCreator = new UserCreator();

        ProjectManager projectManager = userCreator.createManager("Patrick", "yolo");

        assertEquals("Patrick", projectManager.getUsername());
        assertEquals("yolo", projectManager.getPassword());
    }

}