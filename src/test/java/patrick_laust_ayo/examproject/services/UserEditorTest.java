package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

import static org.junit.jupiter.api.Assertions.*;

class UserEditorTest {

    //UserEditor userEditor = new UserEditor();

    @ParameterizedTest
    @CsvSource(value={"2|qscqsc*21|Gitte|Developer|Lone"}, delimiter = '|')
    void updateParticipant(int id, String password, String name, String position, String formerName) {
        //Arrange
        UserEditor userEditor = new UserEditor();

        //Act
        Participant participant = userEditor.updateParticipant(id, password, name, position, formerName);
        //Assert
        assertEquals(participant.getId(), id);
        assertEquals(participant.getPassword(), password);
        assertEquals(participant.getName(), name);
        assertEquals(participant.getPosition(), position);


    }

    @ParameterizedTest
    @CsvSource(value = {"Andy bozz|erAsD14'd|Andy Boss"}, delimiter = '|')
    void updateProjectmanager(String username, String password, String formerUsername) {

        UserEditor userEditor = new UserEditor();

        //act
        ProjectManager projectManager = userEditor.updateProjectmanager(username,password,formerUsername);

        //assert
        assertEquals(projectManager.getUsername(), username);
        assertEquals(projectManager.getPassword(), password);
    }
}