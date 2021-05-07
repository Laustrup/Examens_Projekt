package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.ParticipantRepository;

public class Participant {

    private ParticipantRepository repo;

    private int id;

    private String name;
    private String position;

    private Department department;

    public Participant(int id, String name, String position, Department department) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.department = department;
    }
}
