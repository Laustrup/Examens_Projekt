package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

public class ProjectEditor {

    ProjectRepository repo = new ProjectRepository();

    private Project project;

    public Project updateProject(String title, String formerTitle) {

        // This object doesn't have a password nor a department
        project = new Project(title);

        repo.updateProject(project,formerTitle);
        // Makes sure that it's the real participant from db that is being returned
        ProjectCreator projectCreator = new ProjectCreator();

        return projectCreator.getProject(title);
    }

    public Phase updatePhase(String phaseTitle, String formerphaseTitle, String projectTitle) {
        new ProjectRepository().updatePhase(phaseTitle,projectTitle,formerphaseTitle);
        return new ProjectCreator().getPhase(phaseTitle,projectTitle);
    }

}
