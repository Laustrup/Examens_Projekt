package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;

public class UserEditor {

    private ParticipantRepository participantRepo = new ParticipantRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    private ProjectManager projectManager;
    private Participant participant;

    public Participant updateParticipant(String id, String password, String name, String position, String formerUserId) {
        participantRepo.updateParticipant(id, name, password, position, formerUserId);

        // Makes sure that it's the real participant from db that is being returned
        ResultSet res = participantRepo.findParticipant(formerUserId);
        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getString("user_id"), res.getString("participant_password"),
                    res.getString("participant_name"), res.getString("position"),
                    department);
        } catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }
        return participant;
    }

    public ProjectManager updateProjectmanager(String username, String password, String formerUsername) {
        projectManagerRepo.updateProjectManager(username, password, formerUsername);
        return new UserCreator().getProjectManager(username);
    }

    public Participant removeParticipant(String userId) {
        ResultSet res = participantRepo.findParticipant(userId);

        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getString("user_id"), res.getString("participant_password"),
                    res.getString("participant_name"), res.getString("position"),
                    department);
        } catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }

        participantRepo.removeParticipant(participant.getId());

        return participant;
    }

    // Returns an exception as a string, if project is booked
    public String joinParticipantToProject(Participant participant, Project project) {
        ProjectRepository repo = new ProjectRepository();
        ExceptionHandler handler = new ExceptionHandler();

        if (!(handler.isProjectFullybooked(project))) {
            repo.addParticipantToProject(participant,project);
            return (participant.getId() + " is added!");
        }
        else {
            return "Project is fully booked, projectmanager needs to add more participants of your department...";
        }
    }
}
