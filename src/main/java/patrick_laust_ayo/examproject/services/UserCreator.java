package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;
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

    public Participant createParticipant(String projectTitle) {
        ParticipantRepository parRepo = new ParticipantRepository();
        ProjectRepository proRepo = new ProjectRepository();

        participant = new Participant(parRepo.calcNextId("participant"), new String(), new String(), new String(), null);

        if (proRepo.doesProjectExist(projectTitle)) {
            parRepo.putParticipantInDatabase(participant, proRepo.findId("project","title",projectTitle,
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
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);

                Participant tempParticipant = new Participant(password, username);

                participantMap.put(password, tempParticipant);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return participantMap;
    }

    public boolean doesParticipantExist(String password){
        Map<String, Participant> userList = getParticipantMap();
        return userList.containsKey(password);

    }

}
}
