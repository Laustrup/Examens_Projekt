package patrick_laust_ayo.examproject.models;

import java.util.ArrayList;

public class Task extends Assignment {

    private double estimatedWorkHours;
    private ArrayList<Participant> participants;

    public Task(double estimatedWorkHours,ArrayList<Participant> participants) {
        super(null,null,null,false,null);
        this.estimatedWorkHours = estimatedWorkHours;
        this.participants = participants;
    }

    public Task(double estimatedWorkHours,ArrayList<Participant> participants,
                String start,String end,String title,boolean isCompleted) {
        super(start,end,title,isCompleted,null);
        this.estimatedWorkHours = estimatedWorkHours;
        this.participants = participants;
    }

    public double getEstimatedWorkHours() {
        return estimatedWorkHours;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setEstimatedWorkHours(double estimatedWorkHours) {
        this.estimatedWorkHours = estimatedWorkHours;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public double totalCost() {
        System.out.println("Task cost = " + participants.size() * estimatedWorkHours * 37.5);
        return participants.size() * estimatedWorkHours * 37.5;
    }
}
