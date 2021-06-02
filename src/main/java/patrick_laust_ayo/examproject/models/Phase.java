package patrick_laust_ayo.examproject.models;

import java.util.ArrayList;

//Authors Ayo,Patrick and Laust
public class Phase {

    private ArrayList<Assignment> assignments;

    private String title;


    public Phase(String title) {
        this.title = title;
        this.assignments = new ArrayList<>();

    }

    public Phase(String title, ArrayList<Assignment> assignments) {
        this.title = title;
        this.assignments = assignments;
    }


    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void addToAssignments(Assignment assignment) {
        assignments.add(assignment);
    }

    public double getTotalWorkhours() {
        double total = 0;
        for (int i = 0; i < assignments.size();i++) {
            total += assignments.get(i).getTotalAssignmentWorkhours();
        }

        return total;
    }

    public double getTotalCost() {
        double total = 0;
        for (int i = 0; i < assignments.size();i++) {
            total += assignments.get(i).getTotalAssignmentCost();
        }

        return total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }
}
