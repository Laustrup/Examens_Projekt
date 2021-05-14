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

    public Participant updateParticipant(int id, String name, String position) {

        // This object doesn't have a password nor a department
        participant = new Participant(id,name,position);

        pRepo.updateParticipant(participant);

        return participant;
    }

    public ProjectManager updateProjectmanager(String username, String password, String formerUsername) {

        projectManager = new ProjectManager(username, password);

        pmRepo.updateProjectManager(projectManager, formerUsername);

        return projectManager;

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
