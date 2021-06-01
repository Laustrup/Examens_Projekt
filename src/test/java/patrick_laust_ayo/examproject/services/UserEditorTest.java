package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

import static org.junit.jupiter.api.Assertions.*;

class UserEditorTest {


    @ParameterizedTest
    @CsvSource(value={"lone9242|qscqsc*21|Gitte|Developer|OSLO|lone9242,false"}, delimiter = '|')
    void updateParticipant(String id, String password, String name, String position, String departmentName, String formerUserId, boolean isProjectManager) {
        //Arrange
        UserEditor userEditor = new UserEditor();

        //Act
        Participant participant = userEditor.updateParticipant(id, password, name, position, departmentName,formerUserId,isProjectManager);
        //Assert
        assertEquals(participant.getId(), id);
        assertEquals(participant.getPassword(), password);
        assertEquals(participant.getName(), name);
        assertEquals(participant.getPosition(), position);


    }

    @ParameterizedTest
    @CsvSource(value = {"Andy boss|erAsD14d|andy0432","andy0432|erAsD14d|Andy Boss" }, delimiter = '|')
    void updateProjectmanager(String username, String formerUsername) {

        UserEditor userEditor = new UserEditor();

        //act
        ProjectManager projectManager = userEditor.updateProjectmanager(username,formerUsername);

        //assert
        assertEquals(projectManager.getUsername(), username);
    }
}