package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

public class UserEditor {

    private ParticipantRepository participantRepo = new ParticipantRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    private ProjectManager projectManager;
    private Participant participant;

    public Participant updateParticipant(String id, String password, String name, String position, String userId) {
        participantRepo.updateParticipant(id, name, password, position, userId);

        // Makes sure that it's the real participant from db that is being returned
        participant = participantRepo.findParticipant(userId);

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

    public boolean isInputInteger(String input) {
        try {
            int parsedInput = Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
