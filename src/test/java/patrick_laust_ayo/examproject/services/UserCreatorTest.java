package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatorTest {

    private UserCreator userCreator = new UserCreator();
    private ProjectRepository repo = new ProjectRepository();

    @ParameterizedTest
    @CsvSource(value = {"1"}, delimiter = '|')
    public void createParticipantTest(String expected) {
        //Arrange
        String[] expectations = expected.split("_");
        Participant participant;

        //Act
        participant = userCreator.createParticipant("Appdev", "COPENHAGEN");

        //Assert
        assertEquals(String.valueOf(repo.findForeignId("participant", "project_id", String.valueOf(repo.calcNextId("project")-1),"project_id")),expectations[0]);
    }
}