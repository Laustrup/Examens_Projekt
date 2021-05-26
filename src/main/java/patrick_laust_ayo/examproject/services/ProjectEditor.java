package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Assignment;
import patrick_laust_ayo.examproject.models.Phase;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.Task;
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

    public Assignment updateAssignment(String title,String start,String end, String formerTitle,String phaseTitle) {
        repo.updateAssignment(title, start, end, formerTitle, phaseTitle);
        return creator.getAssignment(title,phaseTitle);
    }
    public Assignment changeIsCompletedAssignment(boolean isCompleted,String assignmentTitle,String phaseTitle) {
        if (isCompleted) {
            repo.updateAssignmentIsCompleted("true",assignmentTitle,phaseTitle);
        }
        else {
            repo.updateAssignmentIsCompleted("false",assignmentTitle,phaseTitle);
        }
        return creator.getAssignment(assignmentTitle,phaseTitle);

    }

    public Task updateTask(String title, String start, String end, double workHours, String formerTitle, String assignmentTitle) {
        repo.updateTask(title, start, end, String.valueOf(workHours), formerTitle, assignmentTitle);
        return creator.getTask(title,start,end);
    }
    public Task changeIsCompletedTask(boolean isCompleted,String taskTitle,String taskStart, String taskEnd,String phaseTitle) {
        Task task = new ProjectCreator().getTask(taskTitle, taskStart, taskEnd);
        if (isCompleted) {
            repo.updateTaskIsCompleted("true",task);
        }
        else {
            repo.updateTaskIsCompleted("false",task);
        }
        return task;
    }

}
