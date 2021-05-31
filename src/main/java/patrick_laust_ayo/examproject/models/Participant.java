package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ParticipantRepository;

public class Participant {

    private String id;
    private String password = new String();
    private String hiddenPassword = new String();
    private String name;
    private String position;
    private Department department;

    public Participant(String id, String password, String name, String position, Department department) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.position = position;
        this.department = department;
        setHiddenPassword();
    }

    public Participant(String id, String password) {
        this.id = id;
        this.password = password;
        setHiddenPassword();
    }

    // Constructor for updating
    public Participant(String id, String password, String name, String position) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.position = position;
        setHiddenPassword();
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String setHiddenPassword() {
        hiddenPassword = new String();
        if (password != null) {
            for (int i = 0; i < password.length(); i++) {
                hiddenPassword += "*";
            }
        }
        return hiddenPassword;
    }

    public String getHiddenPassword() {
        return hiddenPassword;
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
