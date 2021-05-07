package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;


public class ProjectCreator {

    private Project project;

    public Project createProject(String title, String password, String username) {

        ProjectmanagerRepository repo = new ProjectmanagerRepository();

        project = new Project(title, password, null, null, repo.findProjectManagerFromUsername(username));

        return project;
    }
}
