package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Project;


public class ProjectCreator {

    private Project project;

    public Project createProject(String title, String password, String username) {

        project = new Project(title, password, null, null, projectManager);


        return project;
    }
}
