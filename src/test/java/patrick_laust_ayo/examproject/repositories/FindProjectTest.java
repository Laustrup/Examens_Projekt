package patrick_laust_ayo.examproject.repositories;

import org.junit.jupiter.api.Test;
import patrick_laust_ayo.examproject.services.ProjectCreator;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class FindProjectTest {

    @Test
    void findProject() {
        //Arrange
        ProjectRepository pRepo = new ProjectRepository();
        ProjectCreator pCreator = new ProjectCreator();

        //Act
        pCreator.createProject("Cleverbot v.2", "Andy Boss");
        ResultSet res = pRepo.findProject("Cleverbot v.2");

        //Assert
        try {
            res.next();
            assertEquals("Cleverbot v.2", res.getString("title"));
        }
        catch (Exception e){
            System.out.println("Find Project Test went wrong " + e.getMessage());
            e.printStackTrace();
        }
    }
}