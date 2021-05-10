package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);

       ProjectmanagerRepository pmRepo = new ProjectmanagerRepository();
       pmRepo.putProjectManagerInDatabase(projectManager);

       return projectManager;
    }

    public Participant createParticipant(Project project) {
        ParticipantRepository parRepo = new ParticipantRepository();
        ProjectRepository proRepo = new ProjectRepository();
        DepartmentRepository depRepo = new DepartmentRepository();

        participant = new Participant(parRepo.calcNextId("participant"), new String(), new String(), null);

        parRepo.putParticipantInDatabase(participant, proRepo.findProjectId(project), participant.getDepartment().getDepartmentNo());

        return participant;
    }
}
