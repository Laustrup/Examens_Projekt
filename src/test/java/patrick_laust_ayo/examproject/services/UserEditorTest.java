package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

import static org.junit.jupiter.api.Assertions.*;

//Author Patrick
class UserEditorTest {

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