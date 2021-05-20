package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Project;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private ExceptionHandler handler = new ExceptionHandler();

    @ParameterizedTest
    @CsvSource(value = {"ge\"rt0394|ge\\\"rt0394","\"iver423|\\\"iver423", "frn934\"|frn934\\\"", "\"sf\"4235\"|\\\"sf\\\"4235\\\"",
                        "ge'rt0394|ge\\'rt0394","'iver423|\\'iver423", "frn934'|frn934\\'", "'sf'4235'|\\'sf\\'4235\\'",
                        "ge\\rt0394|ge\\\\rt0394","\\iver423|\\\\iver423", "frn934\\|frn934\\\\", "\\sf\\4235\\|\\\\sf\\\\4235\\\\"}
                        ,delimiter = '|')
    public void secureInputToDbTest(String input,String expected) {
        // Arrange
        // Act
        input = handler.secureInputToDb(input);
        // Assert
        assertEquals(expected,input);
    }

    // TODO seems to work, perhaps further parameter for titles
    @ParameterizedTest
    @CsvSource(value = {"afdasg12421456ds|user_id|ID is too long... Write less than 15 words!", "afd12421456ds|user_id|Input is allowed",
                "***************************|participant_password|Password is too long... Write less than 25 words!", "****************|participant_password|Input is allowed"}
            ,delimiter = '|')
    public void isLengthAllowedInDatabaseTest(String input,String column,String expected) {
        // Arrange
        // Act
        String actual = handler.isLengthAllowedInDatabase(input,column);
        // Assert
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @CsvSource(value = {"Appdev|true"},delimiter = '|')
    public void isProjectFullybookedTest(String projectTitle, boolean expected) {
        // Arrange
        ProjectCreator projectCreator = new ProjectCreator();
        Project project = projectCreator.getProject(projectTitle);

        // Act
        boolean actual = handler.isProjectFullybooked(project);
        // Assert
        assertEquals(expected,actual);
    }

}