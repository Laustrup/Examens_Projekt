package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Author Laust
public class ExceptionHandler {

    // Methods checks if objects or attributes exists
    public boolean doesProjectExist(String title){
        if (new ProjectCreator().getProject(title) == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean doesPhaseExist(String phaseTitle, String projectTitle) {
        ResultSet res = new ProjectRepository().findPhase(phaseTitle,projectTitle);
        try {
            res.next();
            res.getString("phase_title");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean doesAssignmentExist(String assignmentTitle, String phaseTitle) {
        if (new ProjectCreator().getAssignment(assignmentTitle, phaseTitle) == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean doesTaskExist(String taskTitle, String taskStart, String taskEnd) {
       if (new ProjectCreator().getTask(taskTitle,taskStart,taskEnd) == null) {
            return false;
        }
       else {
           return true;
        }
    }

    public boolean doesUserIdExist(String userId) {
        ParticipantRepository repo = new ParticipantRepository();

        ResultSet res = repo.selectAll("participant");

        try {
            while (res.next()) {
                if (res.getString("user_id").equals(userId)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Trouble identifying ResultSet when searching user_id...\n" + e.getMessage());
        }
        return false;
    }

    public boolean doesDepartmentExist(String depName) {
        return new UserCreator().getDepartment(depName) != null;
    }

    public boolean doesParticipantExist(String participant_ID){
        Map<String, Participant> userList = getParticipantMap();
        return userList.containsKey(participant_ID);
    }

    private Map<String, Participant> getParticipantMap() {

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
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return participantMap;
    }

    // Allow logins
    public boolean allowLogin(String userId, String password) {
        Participant participant = new UserCreator().getParticipant(userId);

        if (participant.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean isProjectFullybooked(Project project, int departmentNo) {

        if (project.getParticipants().get("Enter user-ID") != null) {
            ResultSet res = new DepartmentRepository().findDepartmentOfEmptyParticipant(departmentNo, project.getTitle());
            try {
                res.next();
                res.getString("department_name");
            }
            catch (Exception e) {
                System.err.println("Project fully booked");
                return true;
            }
        }
        return false;
    }

    public boolean isDateTimeCorrectFormat(String dateTime) {

        if(dateTime.contains("-")&&dateTime.contains(":")&&dateTime.contains(" ")&&dateTime.length()<19) {
            String[] datesAndTime = dateTime.split(" ");
            String[] dates = datesAndTime[0].split("-");
            String[] times = datesAndTime[1].split(":");

            int year;
            int month;
            int day;

            int hours;
            int minutes;
            int seconds;

            if (!(dates.length==3&&times.length==3)) {
                return false;
            }

            try {
                year = Integer.parseInt(dates[0]);
                month = Integer.parseInt(dates[1]);
                day = Integer.parseInt(dates[2]);

                hours = Integer.parseInt(times[0]);
                minutes = Integer.parseInt(times[1]);
                seconds = Integer.parseInt(times[2]);
            }
            catch (Exception e) {
                System.out.println("Couldn't parse dates and times...\n" + e.getMessage());
                return false;
            }

            if (year>10000||month>12||day>31||hours>23||minutes>59||seconds>59) {
                return false;
            }
            return true;
        }
        return false;
    }

    // Accepts if the participant is part of the project
    public boolean isParticipantPartOfProject(String userId, String projectTitle) {

        ArrayList<Project> projects = new ProjectCreator().getProjects(userId);

        for (int i = 0; i < projects.size();i++) {
            if (projects.get(i).getTitle().equals(projectTitle)) {
                return true;
            }
        }
        return false;
    }

    public boolean isParticipantProjectManager(String userId) {

        return new UserCreator().getProjectManager(userId) != null;
    }

    // Checks if input is too long and writes a message as return,
    // if input is allowed, it returns "Input is allowed"
    public String isLengthAllowedInDatabase(String input,String column)  {

        if (inputAsTitleIsTooLongByAmount(input,column) != -1) {
            return column + " is too long... Write less than " + inputAsTitleIsTooLongByAmount(input,column) + " words!";
        }
        if (column.equals("participant_password") && input.length()>25) {
            return "Password is too long... Write less than 25 words!";
        }
        if (column.equals("phase_title") && input.length()>50) {
            return "Title is too long... Write less than 50 charactors";
        }

        return "Input is allowed";
    }

    private int inputAsTitleIsTooLongByAmount(String input,String column) {

        try {
            if (column.equals("username") ||
                    column.equals("participant_name") ||
                    column.equals("position") ||
                    column.equals("user_id")) {
                if (input.length()>25) {
                    return 25;
                }
            }

            if (column.equals("title") ||
                column.equals("department_name")) {
                if (input.length()>30) {
                    return 30;
                }
            }

            if (column.equals("assignment_title") && input.length()>50) {
                return 50;
            }

            if (column.equals("location") && input.length()>100) {
                return 100;
            }
        }
        catch (Exception e) {
            System.out.println("Input is too long...");
        }

        return -1;
    }
}
