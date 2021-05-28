package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

import java.sql.ResultSet;

public class UserCreator {

    private ProjectManager projectManager;
    private Participant participant;

    public ProjectManager createManager(String username, String password) {
       projectManager = new ProjectManager(username, password);

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

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            projectManager = new ProjectManager(res.getString("username"),res.getString("participant_password"),
                    res.getString("user_id"),res.getString("participant_name"),
                    res.getString("position"),department);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
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
            parRepo.putParticipantInDatabase(userId,  participant.getDepartment().getDepartmentNo());
            System.out.println("Departmentet er " + depName + " no " + participant.getDepartment().getDepartmentNo() + " og depname for participant er " + participant.getDepartment().getDepName());
        }
        return participant;
    }

    public Participant createProjectManagerAsParticipant(String userId, String depName) {
        ParticipantRepository parRepo = new ParticipantRepository();

        participant = new Participant(userId, null, null, null, getDepartment(depName));
        parRepo.putParticipantInDatabase(userId,  participant.getDepartment().getDepartmentNo());

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
            System.out.println("Couldn't create a participant from resultSet...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }
        repo.closeCurrentConnection();
        return participant;
    }
}

