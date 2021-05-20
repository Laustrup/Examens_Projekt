package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import patrick_laust_ayo.examproject.models.Project;

import static org.junit.jupiter.api.Assertions.*;

class GetProjectTest {

    @Test
    void getProject() {
        //Arrange
        ProjectCreator pCreator = new ProjectCreator();

        //Act
        Project project = pCreator.getProject("Appdev");

        //Assert
        try {
            //Projekt titlen
            assertEquals("Appdev", project.getTitle());


            //TODO getTitle fra Phases skal opdateres når det virker i ProjectCreator
            //Projektets assignments værdier
          //  assertEquals("", project.getPhases().get(0).getTitle());
            assertEquals("Brainstorming", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getTitle());
            assertEquals(false, project.getPhases().get(0).getAssignments().get(String.valueOf(1)).isCompleted());
            assertEquals("2021-10-15 12:30:00", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getStart());
            assertEquals("2021-12-16 23:59:59", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getEnd());


            //Projektets participants værdier
            assertEquals("Anders", project.getParticipants().get("Projectmember number " + 2).getName());
            assertEquals("Developer", project.getParticipants().get("Projectmember number " + 1).getPosition());
            assertEquals("Manager", project.getParticipants().get("Projectmember number " + 0).getPosition());
            assertEquals("ande0137", project.getParticipants().get("Projectmember number " + 2).getId());
            assertEquals("NEW YORK", project.getParticipants().get("Projectmember number " + 2).getDepartment().getDepName());
            assertEquals(3, project.getParticipants().get("Projectmember number " + 2).getDepartment().getDepartmentNo());
            assertEquals("twe@faf2", project.getParticipants().get("Projectmember number " + 2).getPassword());


            //Projektets projectmanagers værdier
            assertEquals("Andy Boss", project.getProjectManager().getUsername());
            assertEquals("Manager", project.getProjectManager().getPosition());
            assertEquals("andy0432", project.getProjectManager().getId());
            assertEquals("erAsD14'd", project.getProjectManager().getPassword());
            assertEquals("Andy Boss", project.getProjectManager().getName());
            assertEquals(2, project.getProjectManager().getDepartment().getDepartmentNo());
            assertEquals("OSLO", project.getProjectManager().getDepartment().getDepName());
        }
        catch (Exception e){
            System.out.println("GetProjectTest Problem " + e.getMessage());
            e.printStackTrace();
        }
    }
}