package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.util.Map;

public class Phase {

    private ProjectRepository repo;

    private Map<String, Assignment> assignments;

    private String title;

    public Phase(String title) {
        this.title = title;
    }

    public Map<String,Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<String, Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
