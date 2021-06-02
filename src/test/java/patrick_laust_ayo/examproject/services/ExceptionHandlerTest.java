package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

//Author Patrick
class ExceptionHandlerTest {


    private ExceptionHandler handler = new ExceptionHandler();


    @ParameterizedTest
    @CsvSource(value = {"afdasg12421456ds2l34kgdso3|user_id|user_id is too long... Write less than 25 words!",
            "afd12421456ds|user_id|Input is allowed",
            "***************************|participant_password|Password is too long... Write less than" +
                    " 25 words!", "****************|participant_password|Input is allowed"},
            delimiter = '|')
    public void isLengthAllowedInDatabaseTest(String input,String column,String expected) {

        // Act
        String actual = handler.isLengthAllowedInDatabase(input,column);

        // Assert
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @CsvSource(value = {"Appdev|true","Saledev|false"},delimiter = '|')
    public void doesProjectExistTest(String title,boolean expected) {
        // Arrange

        // Act
        boolean actual = handler.doesProjectExist(title);
        // Assert
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @CsvSource(value = {"lone9242|true","dafs1241as|false"},delimiter = '|')
    public void doesUserIdExistTest(String userId,boolean expected) {

        // Act
        boolean actual = handler.doesUserIdExist(userId);
        // Assert
        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @CsvSource(value = {"qscqsc*21|true","sfdafv31½23|false"},delimiter = '|')
    public void allowLoginTest(String password,boolean expected) {

        // Act
        boolean actual = handler.allowLogin("lone9242",password);
        // Assert
        assertEquals(expected,actual);
    }
}