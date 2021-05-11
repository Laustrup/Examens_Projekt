package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectCreator {

    private Project project;

    public Project createProject(String title, String password, String managerName) {

        ProjectmanagerRepository pmRepo = new ProjectmanagerRepository();
        project = new Project(title, password, new ArrayList<>(), new HashMap<>(), pmRepo.findProjectManagerFromUsername(managerName));

        ProjectRepository pRepo = new ProjectRepository();
        pRepo.putProjectInDatabase(project, pmRepo.getCurrentId());

        return project;
    }

}
