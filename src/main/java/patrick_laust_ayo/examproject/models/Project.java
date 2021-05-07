package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.Map;

public class Project {

    private ProjectRepository repo;

    private String password;

    private ArrayList<Phase> phases;

    private Map<String, Participant> participants;
    private ProjectManager projectManager;

    private int totalCost;


    public Project(String password, ArrayList<Phase> phases, Map<String, Participant> participants, ProjectManager projectManager) {
        this.password = password;
        this.phases = phases;
        this.participants = participants;
        this.projectManager = projectManager;
    }
}
