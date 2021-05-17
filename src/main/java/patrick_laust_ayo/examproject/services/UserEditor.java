package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

public class UserEditor {

    private ParticipantRepository pRepo = new ParticipantRepository();
    private ProjectmanagerRepository pmRepo = new ProjectmanagerRepository();

    private ProjectManager projectManager;
    private Participant participant;

    public Participant updateParticipant(String id, String name, String position, String formerName) {

        // This object doesn't have a password nor a department
        participant = new Participant(id,name,position);

        pRepo.updateParticipant(participant,formerName);
        // Makes sure that it's the real participant from db that is being returned
        participant = pRepo.findParticipant(name, true);

        return participant;
    }

    public ProjectManager updateProjectmanager(String username, String password, String formerUsername) {

        projectManager = new ProjectManager(username, password);

        pmRepo.updateProjectManager(projectManager, formerUsername);
        // Makes sure that it's the real projectmanager from db that is being returned
        projectManager = pmRepo.findProjectManager(username);

        return projectManager;
    }

    public Participant removeParticipant(String id) {
        participant = pRepo.findParticipant(id,false);
        pRepo.removeParticipant(participant.getId());

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
