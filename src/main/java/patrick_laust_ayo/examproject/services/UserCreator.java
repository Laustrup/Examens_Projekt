package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectmanagerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);

       ProjectmanagerRepository pmRepo = new ProjectmanagerRepository();
       pmRepo.putProjectManagerInDatabase(projectManager);

       return projectManager;
    }

    public Participant createParticipant(String projectTitle, String depName) {
        ParticipantRepository parRepo = new ParticipantRepository();
        ProjectRepository proRepo = new ProjectRepository();
        DepartmentRepository depRepo = new DepartmentRepository();

        participant = new Participant(parRepo.calcNextId("participant"), new String(), new String(), new String(),
                                                                              depRepo.findDepartmentByName(depName));

        if (proRepo.doesProjectExist(projectTitle)) {
            parRepo.putParticipantInDatabase(participant, proRepo.findForeignId("project","title",projectTitle,
                    "project_id"), participant.getDepartment().getDepartmentNo());
        }

        return participant;
    }

    public Map<Integer, Participant> getParticipantMap() {

        ParticipantRepository participantRepository = new ParticipantRepository();
        Map<Integer, Participant> participantMap = new HashMap<>();

        try {
            ResultSet resultSet = participantRepository.executeQuery("SELECT * FROM participant");

            while (resultSet.next()) {
                int participant_ID = resultSet.getInt(1);
                String username = resultSet.getString(2);


                Participant tempParticipant = new Participant(participant_ID, username);

                participantMap.put(participant_ID, tempParticipant);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return participantMap;
    }

    public boolean doesParticipantExist(int participant_ID){
        Map<Integer, Participant> userList = getParticipantMap();
        return userList.containsKey(participant_ID);

    }

}

