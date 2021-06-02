package patrick_laust_ayo.examproject.models;

//Authors Ayo,Patrick and Laust
public class ProjectManager extends Participant {

    private String username;

    public ProjectManager(String username, String password) {

        super(new String(),password,username,null,null);
        this.username = username;
    }

    public ProjectManager(String username) {

        super(null, null, null, null, null);
        this.username = username;
    }

    public ProjectManager(String username, String password, String participantId,
                          String participantName,String position,Department department) {

        super(participantId,password,participantName,position,department);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
