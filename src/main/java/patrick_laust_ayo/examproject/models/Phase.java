package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.util.HashMap;
import java.util.Map;

public class Phase {

    private Map<String, Assignment> assignments;

    private String title;
    private String[] assignmentTitles = new String[0];

    public Phase(String title) {
        this.title = title;
        this.assignments = new HashMap<>();
        setAssignmentTitles();
    }

    public Phase(String title, Map<String, Assignment> assignments) {
        this.title = title;
        this.assignments = assignments;
        setAssignmentTitles();
    }

    public Map<String,Assignment> getAssignments() {
        return assignments;
    }

    public void putInAssignments(String key, Assignment assignment) {
        assignments.put(key, assignment);
    }

    public double getTotalWorkhours() {
        double total = 0;
        if (assignments!=null) {
            for (int i = 0; i < assignments.size()/2;i++) {
                total += assignments.get(i).getTotalAssignmentWorkhours();
            }
        }
        return total;
    }
    public double getTotalCost() {
        double total = 0;
        if (assignments!=null) {
            for (int i = 0; i < assignments.size()/2;i++) {
                total += assignments.get(i).getTotalAssignmentCost();
            }
        }
        return total;
    }

    public void setAssignmentTitles() {
        assignmentTitles = new String[assignments.size()];

        if (assignments.get(0)!=null) {
            for (int i = 0; i < assignments.size()/2; i++) {
                assignmentTitles[i] = assignments.get(i).getTitle();
            }
        }
    }

    public String[] getAssignmentTitles() {
        return assignmentTitles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAssignments(Map<String, Assignment> assignments) {
        this.assignments = assignments;
    }
}
