package patrick_laust_ayo.examproject.models;

public class Task extends Assignment{

    private double estimatedWorkHours;

    public Task(double estimatedWorkHours) {
        super(null,null,null,false,null,null);
        this.estimatedWorkHours = estimatedWorkHours;
    }

    public double getEstimatedWorkHours() {
        return estimatedWorkHours;
    }

    public void setEstimatedWorkHours(double estimatedWorkHours) {
        this.estimatedWorkHours = estimatedWorkHours;
    }
}
