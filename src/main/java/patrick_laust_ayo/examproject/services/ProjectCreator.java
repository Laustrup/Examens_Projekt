package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectCreator {

    private Project project;
    private Phase phase;
    private Assignment assignment;
    private Task task;

    private ProjectRepository pRepo = new ProjectRepository();
    private ProjectmanagerRepository pmRepo = new ProjectmanagerRepository();

    public Project createProject(String title, String password, String managerName) {

        project = new Project(title, password, new ArrayList<>(), new HashMap<>(), pmRepo.findProjectManager(managerName));

        pRepo.putProjectInDatabase(project, pmRepo.getCurrentId());

        return project;
    }

    public Phase createPhase(String projectTitle) {

        phase = new Phase(new String());

        int id = pRepo.findForeignId("project","title",projectTitle, "project_id");
        pRepo.putPhaseInDatabase(id);

        return phase;
    }

    public Assignment createAssignment(String phaseTitle, String start, String end) {

        assignment = new Assignment(start,end,new String(),false, new ArrayList<Participant>(),new ArrayList<Task>());

        int phaseId = pRepo.findForeignId("phase_table","phase_title",phaseTitle,"phase_id");
        Integer taskId = null;

        pRepo.putAssignmentInDatabase(assignment,phaseId);

        return assignment;
    }

    public Task createTask(String assignmentTitle) {

        Double workHours = null;
        task = new Task(workHours);


        return task;
    }

}
