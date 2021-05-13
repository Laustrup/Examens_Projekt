package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatorTest {

    private UserCreator userCreator = new UserCreator();
    private ProjectRepository repo;

    @ParameterizedTest
    @CsvSource(value = {"1|3|1_3"}, delimiter = '|')
    public void createParticipantTest(String projectId, String iterations, String expected) {
        //Arrange
        //ResultSet res = repo.executeQuery();
        String[] expectations = expected.split("_");

        int amounts = Integer.parseInt(iterations);
        Participant participant;

        //Act
        for (int i = 0; i < amounts; i++) {
            participant = userCreator.createParticipant("UserCreatorTest");
        }


        //Assert
        assertEquals(repo.findForeignId("participant", "project_id", String.valueOf(repo.calcNextId("project")-1),"projectmanager_id"),expectations[0]);
       // assertEquals(project.getPassword(),expectations[1]);
       // assertEquals(project.getProjectManager().getName(),expectations[2]);




    }

}