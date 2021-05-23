package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Assignment;
import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

public class ProjectEditor {

    private ProjectRepository repo = new ProjectRepository();
    private ProjectCreator creator = new ProjectCreator();

    public Project updateProject(String title, String formerTitle) {
        repo.updateProject(title,formerTitle);
        return creator.getProject(title);
    }

    public void deleteProject(String title) {
        repo.removeProject(title);
    }
    public void deletePhase(String phaseTitle,String projectTitle) {
        repo.removePhase(phaseTitle,projectTitle);
    }
    public void deleteAssignment(String assignmentTitle,String phaseTitle) {
        repo.removeAssignment(assignmentTitle,phaseTitle);
    }
    public void deleteTask(String taskTitle,String assignmentTitle) {
        repo.removeTask(taskTitle,assignmentTitle);
    }


    public Phase updatePhase(String phaseTitle, String formerphaseTitle, String projectTitle) {
        repo.updatePhase(phaseTitle,projectTitle,formerphaseTitle);
        return creator.getPhase(phaseTitle,projectTitle);
    }

    public Assignment updateAssignmentTitle(String title,String start,String end,boolean isCompleted,
                                            String formerTitle,String phaseTitle) {
        if (isCompleted) {
            repo.updateAssignment(title, start, end, "true", formerTitle, phaseTitle);
        }
        else {
            repo.updateAssignment(title, start, end, "false", formerTitle, phaseTitle);
        }

        return creator.getAssignment(title,phaseTitle);
    }

}
