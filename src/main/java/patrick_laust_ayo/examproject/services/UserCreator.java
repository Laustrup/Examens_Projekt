package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

import java.sql.ResultSet;
import java.util.ArrayList;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    //TODO denne metode er ikke længere nødvendig umiddelbart, fordi den under den, kan erstatte den!
    /*
    public ProjectManager createManager(String username, String password, int departmentNo) {
       projectManager = new ProjectManager(username, password);

       ProjectManagerRepository pmRepo = new ProjectManagerRepository();
       ParticipantRepository parRepo = new ParticipantRepository();

       pmRepo.putProjectManagerInDatabase(projectManager);

       return projectManager;
    }
*/


    public ProjectManager createManager(String username) {
        projectManager = new ProjectManager(username);

        ProjectManagerRepository pmRepo = new ProjectManagerRepository();

        pmRepo.putProjectManagerInDatabase(projectManager);

        return projectManager;
    }

    public Department getDepartment(String depName) {
        DepartmentRepository depRepo = new DepartmentRepository();
        ResultSet res = depRepo.findDepartment(depName);

        Department department = null;

        try{
            res.next();
            department = new Department(res.getInt(1), res.getString(2), res.getString(3));
        }
        catch(Exception e){
            System.out.println("Couldn't find department by name " + e.getMessage());
        }
        depRepo.closeCurrentConnection();
        return department;
    }

    public ProjectManager getProjectManager(String username) {

        ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

        ResultSet res = projectManagerRepo.findProjectManager(username);
        try {
            res.next();
            //TODO Department skal først skabes i participant-delen, så projectmanager skal kun have username
/*
            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            */
            projectManager = new ProjectManager(res.getString("username"));
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet in getProjectManager...\n" + e.getMessage());
            e.printStackTrace();
            projectManager = null;
        }
        projectManagerRepo.closeCurrentConnection();
        return projectManager;
    }

    public Participant createParticipant(String userId, String projectTitle, String depName) {
        ParticipantRepository parRepo = new ParticipantRepository();

        participant = new Participant(userId, null, null, null, getDepartment(depName));
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        if (exceptionHandler.doesProjectExist(projectTitle)) {
            parRepo.putParticipantsInDatabase(userId, participant.getDepartment().getDepartmentNo(),1);
            parRepo.putParticipantsInParticipantProjectTable(userId, projectTitle,1);
        }
        return participant;
    }

    public ArrayList<Participant> createParticipants(String userId, String projectTitle, String depName, int amount) {
        ParticipantRepository parRepo = new ParticipantRepository();
        ArrayList<Participant> participants = new ArrayList<>();

        parRepo.putParticipantsInDatabase(userId, getDepartment(depName).getDepartmentNo(),amount);
        for (int i = 0; i < amount;i++) {
            participants.add(new Participant(userId, null, null, null, getDepartment(depName)));
        }

        parRepo.putParticipantsInParticipantProjectTable(userId, projectTitle,amount);

        return participants;
    }

    public Participant createProjectManagerAsParticipant(String userId, String depName, String projectTitle) {
        ParticipantRepository parRepo = new ParticipantRepository();

        participant = new Participant(userId, null, null, null, getDepartment(depName));
        parRepo.putParticipantsInDatabase(userId,  participant.getDepartment().getDepartmentNo(),1);
        parRepo.putParticipantsInParticipantProjectTable(userId, projectTitle,1);

        return participant;
    }

    public Participant getParticipant(String userId) {
        ParticipantRepository repo = new ParticipantRepository();

        ResultSet res = repo.findParticipant(userId);
        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getString("user_id"), res.getString("participant_password"),
                    res.getString("participant_name"), res.getString("position"),
                    department);
        } catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet in getParticipant...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }
        repo.closeCurrentConnection();
        return participant;
    }
}

