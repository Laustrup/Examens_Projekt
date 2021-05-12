package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ParticipantRepository;

public class Participant {

    private int id;
    private String password;
    private String name;
    private String position;
    private Department department;

    public Participant(int id, String password, String name, String position, Department department) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.position = position;
        this.department = department;
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
    public Participant(String password, String name){
        this.password = password;
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
