package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Assignment {

    private ProjectRepository repo;

    private LocalDateTime start;
    private LocalDateTime end;

    private String title;

    private boolean isCompleted;

    private ArrayList<Participant> participants;
    private ArrayList<Task> tasks;

    public Assignment(LocalDateTime start, LocalDateTime end, String title, boolean isCompleted, ArrayList<Participant> participants, ArrayList<Task> tasks) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.isCompleted = isCompleted;
        this.participants = participants;
        this.tasks = tasks;
    }
}
