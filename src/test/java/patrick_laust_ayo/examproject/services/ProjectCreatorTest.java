package patrick_laust_ayo.examproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.Repository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectCreatorTest {

    private ProjectCreator projectCreator = new ProjectCreator();

    @ParameterizedTest
    @CsvSource(value = {"Save the homeless|Andy boss"}, delimiter = '|')
    public void createProjectTest(String title, String managerName) {
        //Act
        Project actual = projectCreator.createProject(title,managerName);

        //Assert
        assertEquals(actual.getTitle(),title);
        assertEquals(actual.getProjectManager().getUsername(),managerName);
    }

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
            //Projektets fases titel
            assertEquals("Planning", project.getPhases().get(0).getTitle());


            //Projektets assignments værdier
            assertEquals("Brainstorming", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getTitle());
            assertEquals(false, project.getPhases().get(0).getAssignments().get(String.valueOf(1)).isCompleted());
            assertEquals("2021-10-15 12:30:00", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getStart());
            assertEquals("2021-12-16 23:59:59", project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getEnd());


            //Projektets participants værdier
            assertEquals("Anders", project.getParticipants().get("ande0137").getName());
            assertEquals("Developer", project.getParticipants().get("lone9242").getPosition());
            assertEquals("Manager", project.getParticipants().get("andy0432").getPosition());
            assertEquals("ande0137", project.getParticipants().get("ande0137").getId());
            assertEquals("NEW YORK", project.getParticipants().get("ande0137").getDepartment().getDepName());
            assertEquals(3, project.getParticipants().get("ande0137").getDepartment().getDepartmentNo());
            assertEquals("twe@faf2", project.getParticipants().get("ande0137").getPassword());


            //Projektets tasks værdier
            assertEquals(300, project.getPhases().get(0).getAssignments().get(String.valueOf(1)).getTasks().get(0).getEstimatedWorkHours());


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

    // Only tests title since the method getProject is being fully tested through
    // TODO Perhaps add more parameters to test multiple tests
    @ParameterizedTest
    @CsvSource(value = {"lone9242|1|Appdev"}, delimiter = '|')
    public void getProjectsTest(String userId, String amounts, String expected) {
        //Act
        ArrayList<Project> actual = projectCreator.getProjects(userId);

        //Assert
        for (int i = 0; i < Integer.parseInt(amounts);i++) {
            assertEquals(actual.get(0).getTitle(),expected);
        }
    }

    // TODO Perhaps add more parameters to test multiple tests
    // TODO Change safe update to unsafe
    @ParameterizedTest
    @CsvSource(value = {"Appdev|Building"}, delimiter = '|')
    public void createPhaseTest(String projectTitle,String phaseTitle) {
        //Act
        Phase actualReturned = projectCreator.createPhase(projectTitle);
        new ProjectEditor().updatePhase(phaseTitle,null,projectTitle);
        Phase actualFromDb = projectCreator.getPhase(actualReturned.getTitle(),projectTitle);

        //Assert
        assertEquals(new String(), actualReturned.getTitle());
        assertEquals(projectTitle,actualFromDb.getTitle());
    }

}