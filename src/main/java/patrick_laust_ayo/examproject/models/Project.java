package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.Map;

public class Project {

    private ProjectRepository repo;

    private String password;
    private String title;

    private ArrayList<Phase> phases;

    private Map<String, Participant> participants;
    private ProjectManager projectManager;

    private int totalCost;

    public Project(String title, String password, ArrayList<Phase> phases, Map<String,
                    Participant> participants, ProjectManager projectManager) {
        this.title = title;
        this.password = password;
        this.phases = phases;
        this.participants = participants;
        this.projectManager = projectManager;
        calculateTotalCost();
    }


    public int calculateTotalCost() {
        for (int i = 0; i < phases.size(); i++) {
            //phases.get(i).getAssignments().forEach();
        }
        return 0;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
