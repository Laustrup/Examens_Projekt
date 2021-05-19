package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

public class UserEditor {

    private ParticipantRepository participantRepo = new ParticipantRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    private ProjectManager projectManager;
    private Participant participant;

    public Participant updateParticipant(String id, String password, String name, String position, String formerUserId) {
        participantRepo.updateParticipant(id, name, password, position, formerUserId);

        // Makes sure that it's the real participant from db that is being returned
        participant = participantRepo.findParticipant(formerUserId);

        return participant;
    }

    public ProjectManager updateProjectmanager(String username, String password, String formerUsername) {

        projectManager = new ProjectManager(username, password);

        projectManagerRepo.updateProjectManager(projectManager, username, password, formerUsername);
        // Makes sure that it's the real projectmanager from db that is being returned
        projectManager = projectManagerRepo.findProjectManager(username);

        return projectManager;
    }

    public Participant removeParticipant(String userId) {
        participant = participantRepo.findParticipant(userId);
        participantRepo.removeParticipant(participant.getId());

        return participant;
    }

    // Returns an exception as a string, if project is booked
    public String joinParticipantToProject(Participant participant, Project project) {
        ProjectRepository repo = new ProjectRepository();
        ExceptionHandler handler = new ExceptionHandler();

        if (!(handler.isProjectFullybooked(project))) {
            repo.addParticipantToProject(participant,project);
            return (participant.getId() + " is added!");
        }
        else {
            return "Project is fully booked, projectmanager needs to add more participants of your department...";
        }
    }
}
