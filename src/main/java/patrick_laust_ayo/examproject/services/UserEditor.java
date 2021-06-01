package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;


public class UserEditor {

    private ParticipantRepository participantRepo = new ParticipantRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    private Participant participant;


    public Participant updateParticipant(String id, String password, String name, String position,
                                         String departmentName, String formerUserId, boolean isProjectManager) {
        if (isProjectManager) {
            updateProjectmanager(id,formerUserId);
        }

        participantRepo.updateParticipant(id, name, password, position, departmentName, formerUserId);


        // Makes sure that it's the real participant from db that is being returned
        ResultSet res = participantRepo.findParticipant(id);

        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getString("user_id"), res.getString("participant_password"),
                    res.getString("participant_name"), res.getString("position"),
                    department);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet in updateParticipant...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }

        return participant;
    }

    public ProjectManager updateProjectmanager(String username, String formerUsername) {

        projectManagerRepo.updateProjectManager(username, formerUsername);
        return new UserCreator().getProjectManager(username);
    }

    public void removeParticipant(String userId) {

        participantRepo.removeParticipant(userId);
    }

    public void removeProjectManager(String userName) {

        projectManagerRepo.removeProjectManager(userName);
    }

    public String joinParticipantToProject(Participant participant, Project project) {

        ProjectRepository repo = new ProjectRepository();
        ExceptionHandler handler = new ExceptionHandler();


        if (!(handler.isProjectFullybooked(project,participant.getDepartment().getDepartmentNo()))) {
            new UserCreator().createParticipant(participant.getId(),participant.getPassword(),participant.getName(),participant.getPosition(),
                                                project.getTitle(),participant.getDepartment().getDepName());

            repo.addParticipantToProject(participant,project);
            return (participant.getId() + " is added!");
        }
        else {
            return "Project is fully booked, projectmanager needs to add more participants of your department...";
        }
    }

    public String joinParticipantToTask(String userId,Task task) {
        ProjectRepository projectRepository = new ProjectRepository();

        if (projectRepository.addParticipantToTask(userId,task)) {
            return "You are now added to ";
        }
        else {
            return "Couldn't add you to ";
        }
    }

    public String removeParticipantFromTask(String userId, Task task) {

        ResultSet participantRes = participantRepo.findParticipant(userId);
        ProjectRepository projectRepository = new ProjectRepository();
        ResultSet taskRes = projectRepository.findTask(task.getTitle(),task.getStart(),task.getEnd());

        try {
            participantRepo.removeParticipantFromTask(participantRes.getInt("participant_id"),
                    taskRes.getInt("task_id"));
        }
        catch (Exception e) {
            System.out.println("Couldn't remove participant from task...\n" + e.getMessage());
            return "Couldn't remove you from ";
        }

        participantRepo.closeCurrentConnection();
        projectRepository.closeCurrentConnection();

        return "You are now removed from ";
    }
}
