package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

public class ProjectManager extends Participant {

    private ProjectmanagerRepository repo = new ProjectmanagerRepository();

    private String username;
    private String password;

    public ProjectManager(String username, String password) {
        super(0,null,null,null);
        this.username = username;
        this.password = password;
    }

    public ProjectManager(String username, String password, int participantId,
                          String participantName,String position,Department department) {
        super(participantId,participantName,position,department);
        this.username = username;
        this.password = password;
    }
}
