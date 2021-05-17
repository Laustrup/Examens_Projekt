package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;

import static org.junit.jupiter.api.Assertions.*;

class UserEditorTest {


    @ParameterizedTest
    @CsvSource(value={"2|qscqsc*21|Gitte|Developer|Lars"}, delimiter = '|')
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

    @Test
    void updateProjectmanager() {
    }
}