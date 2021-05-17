package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectCreator {

    private Project project;
    private Phase phase;
    private Assignment assignment;
    private Task task;

    private ProjectRepository projectRepo = new ProjectRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    public Project createProject(String title, String managerName) {

        project = new Project(title, new ArrayList<>(), new HashMap<>(), projectManagerRepo.findProjectManager(managerName));

        projectRepo.putProjectInDatabase(project, projectManagerRepo.getCurrentId());

        return project;
    }

    public Phase createPhase(String projectTitle) {

        phase = new Phase(new String());

        int id = projectRepo.findId("project","title",projectTitle, "project_id");
        projectRepo.putPhaseInDatabase(id);

        return phase;
    }

    public Assignment createAssignment(String phaseTitle, String start, String end) {

        assignment = new Assignment(start,end,new String(),false, new ArrayList<Participant>(),new ArrayList<Task>());

        int phaseId = projectRepo.findId("phase_table","phase_title",phaseTitle,"phase_id");
        Integer taskId = null;

        projectRepo.putAssignmentInDatabase(assignment,phaseId);

        return assignment;
    }

    public Task createTask(String assignmentTitle) {

        Double workHours = null;
        task = new Task(workHours);

        projectRepo.putTaskInDatabase(projectRepo.findId("assignment","assignment_title",assignmentTitle,"assignment_id"));

        return task;
    }

}
