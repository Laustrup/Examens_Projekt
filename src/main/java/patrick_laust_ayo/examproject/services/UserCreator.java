package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);

       ProjectManagerRepository pmRepo = new ProjectManagerRepository();
       pmRepo.putProjectManagerInDatabase(projectManager);

       return projectManager;
    }

    public Participant createParticipant(String projectTitle, String depName) {
        ParticipantRepository parRepo = new ParticipantRepository();
        ProjectRepository proRepo = new ProjectRepository();
        DepartmentRepository depRepo = new DepartmentRepository();

        participant = new Participant(new String(), null, null, null,
                                                                              depRepo.findDepartment(depName));
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (exceptionHandler.doesProjectExist(projectTitle)) {
            parRepo.putParticipantInDatabase(proRepo.findId("project","title",projectTitle,
                    "project_id"), participant.getDepartment().getDepartmentNo());
        }

        return participant;
    }

    public Map<String, Participant> getParticipantMap() {

        ParticipantRepository participantRepository = new ParticipantRepository();
        Map<String, Participant> participantMap = new HashMap<>();

        try {
            ResultSet resultSet = participantRepository.executeQuery("SELECT * FROM participant");

            while (resultSet.next()) {
                String participant_ID = resultSet.getString("user_id");
                String username = resultSet.getString("participant_name");

                Participant tempParticipant = new Participant(participant_ID, username);

                participantMap.put(participant_ID, tempParticipant);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return participantMap;
    }

    public boolean doesParticipantExist(String participant_ID){
        Map<String, Participant> userList = getParticipantMap();
        return userList.containsKey(participant_ID);
    }

}

