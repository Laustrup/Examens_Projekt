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
}
