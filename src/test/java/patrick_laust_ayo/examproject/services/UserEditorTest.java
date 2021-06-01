package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

import static org.junit.jupiter.api.Assertions.*;

class UserEditorTest {
//TODO !!!!!!!!!
/*
    @ParameterizedTest
    @CsvSource(value={"loneT|qscqsc*21|Lone Therkilsen|Developer|OSLO|lone9242"}, delimiter = '|')
    void updateParticipant(String newUserId, String password, String name, String position, String departmentName, String formerUserId) {

        //Arrange
        UserEditor userEditor = new UserEditor();

        //Act
        Participant participant = userEditor.updateParticipant(newUserId, password, name, position, departmentName,formerUserId,false);

        //Assert
        assertEquals(participant.getId(), newUserId);
        assertEquals(participant.getPassword(), password);
        assertEquals(participant.getName(), name);
        assertEquals(participant.getPosition(), position);
    } */

    @ParameterizedTest
    @CsvSource(value = {"Andy boss|andy0432","Andy Cool Guy|Andy boss", "andy0432|Andy Cool Guy"},
            delimiter = '|')
    void updateProjectmanager(String username, String formerUsername) {

        //Arrange
        UserEditor userEditor = new UserEditor();

        //Act
        ProjectManager projectManager = userEditor.updateProjectmanager(username,formerUsername);

        //Assert
        assertEquals(projectManager.getUsername(), username);
    }
}