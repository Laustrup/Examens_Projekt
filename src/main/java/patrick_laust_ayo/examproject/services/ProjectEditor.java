package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Assignment;
import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;

public class ProjectEditor {

    private ProjectRepository repo = new ProjectRepository();
    private ProjectCreator creator = new ProjectCreator();

    public Project updateProject(String title, String formerTitle) {
        repo.updateProject(title,formerTitle);
        return creator.getProject(title);
    }

    public Phase updatePhase(String phaseTitle, String formerphaseTitle, String projectTitle) {
        repo.updatePhase(phaseTitle,projectTitle,formerphaseTitle);
        return creator.getPhase(phaseTitle,projectTitle);
    }

    public Assignment updateAssignmentTitle(String column,String updateValue,String assignmentTitle,String phaseTitle) {
        repo.updateAssignment(column,updateValue,assignmentTitle,phaseTitle);
        return creator.getAssignment(assignmentTitle,phaseTitle);
    }

}
