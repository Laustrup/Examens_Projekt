package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);
       return projectManager;
    }

    public Participant createParticipant(int id, String name, String position, Department department) {
        ParticipantRepository repo = new ParticipantRepository();

        participant = new Participant(id, new String(), new String(), null);

        return participant;
    }
}
