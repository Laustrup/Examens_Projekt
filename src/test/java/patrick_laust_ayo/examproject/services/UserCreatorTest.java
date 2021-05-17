package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import patrick_laust_ayo.examproject.models.Participant;
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
}