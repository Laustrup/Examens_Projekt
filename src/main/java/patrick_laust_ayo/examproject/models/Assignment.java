package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Assignment {

    private ProjectRepository repo;

    private String start;
    private String end;

    private String title;

    private boolean isCompleted;

    private ArrayList<Task> tasks;

    public Assignment(String start, String end, String title, boolean isCompleted, ArrayList<Task> tasks) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.isCompleted = isCompleted;
        this.tasks = tasks;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String IsCompletedAsAString() {
        if (isCompleted) {
            return "Completed!";
        }
        else {
            return "Not completed...";
        }
    }

    public double getTotalAssignmentWorkhours() {
        double total = 0;
        for (int i = 0; i < tasks.size();i++) {
            total += tasks.get(i).getEstimatedWorkHours();
        }
        System.out.println("Total assignment hours " + total);
        return total;
    }

    public double getTotalAssignmentCost() {
        double total = 0;
        for (int i = 0; i < tasks.size();i++) {
            total += tasks.get(i).totalCost();
        }
        System.out.println("total assignment cost " + total);
        return total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
