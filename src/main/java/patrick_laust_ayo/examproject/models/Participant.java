package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ParticipantRepository;

public class Participant {

    private String id;
    private String username;
    private String password;
    private String name;
    private String position;
    private Department department;

    public Participant(String id, String password, String name, String position, Department department) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.position = position;
        this.department = department;
    }

    public Participant(String id, String username) {
        this.id = id;
        this.username = username;
    }

    // Constructor for updating
    public Participant(String id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
