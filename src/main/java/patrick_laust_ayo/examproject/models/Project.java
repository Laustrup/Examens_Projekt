package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.Map;

public class Project {

    private String title;

    private ArrayList<Phase> phases = new ArrayList<>();

    private Map<String, Participant> participants;
    private ProjectManager projectManager;

    private double totalCost;
    private int totalWorkHours;

    public Project(String title, ArrayList<Phase> phases, Map<String,
                    Participant> participants, ProjectManager projectManager) {
        this.title = title;
        this.phases = phases;
        this.participants = participants;
        this.projectManager = projectManager;

    }

    // For updating title or password
    public Project(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public ArrayList<Phase> getPhases(){
        return phases;
    }

    public void setPhases(ArrayList<Phase> phases) {
        this.phases = phases;
    }

    public int getTotalWorkhours() {
        totalWorkHours = 0;
        for (int i = 0; i < phases.size();i++) {
            totalWorkHours += phases.get(i).getTotalWorkhours();
        }
        return totalWorkHours;
    }

    public double getTotalCost() {
        totalCost = 0;
        for (int i = 0; i < phases.size();i++) {
            totalCost += phases.get(i).getTotalCost();
        }
        return totalCost;
    }

    public Map<String, Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Participant> participants) {
        this.participants = participants;
    }

}
