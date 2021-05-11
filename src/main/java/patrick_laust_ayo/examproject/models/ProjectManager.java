package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

public class ProjectManager extends Participant {

    private ProjectmanagerRepository repo = new ProjectmanagerRepository();

    private String username;

    public ProjectManager(String username, String password) {
        super(0,password,null,null,null);
        this.username = username;
    }

    public ProjectManager(String username, String password, int participantId,
                          String participantName,String position,Department department) {
        super(participantId,password,participantName,position,department);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
