package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

//Authors Ayo,Patrick and Laust
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
        userCreator.createParticipant("Åge","Appdev", "COPENHAGEN");
        actualId = projectRepo.calcNextId("participant") -1;

        //Assert
        assertEquals(expectedId,actualId);
    }

    @Test
    public void getProjectManagerFromRepositoryTest(){

        //Arrange
        ProjectManagerRepository pmRepo = new ProjectManagerRepository();
        String username = "andy0432";

        int departmentNo = 0;
        String departmentName = "";
        String location = "";

        String password = "";
        String userId = "";
        String name = "";
        String position = "";



        //Act
        String sql = "SELECT * FROM projectmanager " +
                "INNER JOIN participant ON participant.user_id = projectmanager.username " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE username = \"" + username + "\";";

        try {
            ResultSet res = pmRepo.executeQuery(sql);
            res.next();

            departmentNo = res.getInt("department_no");
            departmentName = res.getString("department_name");
            location = res.getString("location");

            password = res.getString("participant_password");
            userId = res.getString("user_id");
            name = res.getString("participant_name");
            position = res.getString("position");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Assert
        assertEquals(2, departmentNo);
        assertEquals("0152 Oslo Norway - Tollbugata 8 A/B", location);
        assertEquals("OSLO", departmentName);
        assertEquals("erAsD14d", password);
        assertEquals("andy0432", userId);
        assertEquals("Andy Boss", name);
        assertEquals("Manager", position);
    }

    @Test
    void createManager(){

        //Arrange
        UserCreator userCreator = new UserCreator();
        ProjectManagerRepository pmRepo = new ProjectManagerRepository();
        String username ="";
        int participantId = 0;

        //Act
        ProjectManager projectManager = userCreator.createManager("Patrick");
        String sql = ("SELECT * FROM projectmanager WHERE username = \"" +
                                        projectManager.getUsername() + "\";");
        try {
            ResultSet res = pmRepo.executeQuery(sql);
            res.next();
            username = res.getString("username");
            participantId = res.getInt("participant_id");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        assertEquals(projectManager.getUsername(), username);
        assertEquals(1, participantId);
    }

    @Test
    void getParticipant(){
        UserCreator userCreator = new UserCreator();

        Participant participant = userCreator.getParticipant("lone9242");

        assertEquals("lone9242", participant.getId());
    }

}