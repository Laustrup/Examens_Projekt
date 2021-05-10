package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);
       return projectManager;
    }

    public Participant createParticipant(int id, String name, String position, Department department) {
        participant = new Participant(id, "Null", "Null", department);
        return participant;
    }
}
