package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Department;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {

    // Was meant to take numbervalues from id for the purpose of db id, but that purpose changed
    /*
    public int returnIdInt(String id) {
        char[] chars = id.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(Character.isDigit(c)){
                sb.append(c);
            }
        }
        try {
            return Integer.parseInt(String.valueOf(sb));
        }
        catch (Exception e) {
            System.out.println("Couldn't parse id to int...\n" + e.getMessage());
            return -1;
        }
    }
    public boolean idAlreadyExistInDb(String id,String table, String column) {

        if (table.equals("task")) {
            table += "_table";
        }

        int idInput = returnIdInt(id);

        Repository repo = new ParticipantRepository();
        ResultSet res = repo.selectAll(table);
        try {
            while (res.next()) {
                if (res.getInt(column) == idInput) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't figure if id already exist...\n" + e.getMessage());
        }

        return false;
    }

     */

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
        if (new ProjectCreator().getPhase(phaseTitle,projectTitle) == null) {
            return false;
        }
        else {
            return true;
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

    /*
    public boolean doesProjectManagerUsernameExist(String username) {
        ProjectManagerRepository repo = new ProjectManagerRepository();

        ResultSet res = repo.selectAll("projectmanager");

        try {
            while (res.next()) {
                if (res.getString("username").equals(username)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Trouble identifying ResultSet when searching username...\n" + e.getMessage());
        }
        return false;
    }

     */
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return participantMap;
    }

    // Allows logins
    public boolean allowLogin(String userId, String password) {
        ParticipantRepository repo = new ParticipantRepository();

        ResultSet res = repo.selectAll("participant");

        try {
            while (res.next()) {
                if (res.getString("participant_password").equals(password) && res.getString("user_id").equals(userId)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Trouble identifying ResultSet when searching user_id...\n" + e.getMessage());
        }
        return false;
    }
    public boolean isProjectFullybooked(Project project, int departmentNo, String userId) {
        for (int i = 1; i <= project.getParticipants().size(); i++) {
            Participant participant = project.getParticipants().get(userId);
            if ((participant.getId() == null || participant.getPassword() == null) &&
                    participant.getDepartment().getDepartmentNo() == departmentNo) {
                return false;
            }
        }
        return true;
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

    // TODO Perhaps unmake repository as abstract?
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

    // Checks if input is too long and writes a message as return, if input is allowed, it returns "Input is allowed"
    public String isLengthAllowedInDatabase(String input,String column)  {

        if (inputAsTitleIsTooLongByAmount(input,column) != -1) {
            return "Title is too long... Write less than " + inputAsTitleIsTooLongByAmount(input,column) + " words!";
        }
        if (column.equals("participant_password") && input.length()>25) {
            return "Password is too long... Write less than 25 words!";
        }
        if (column.equals("user_id") && input.length()>15) {
            return "ID is too long... Write less than 15 words!";
        }

        return "Input is allowed";

    }
    private int inputAsTitleIsTooLongByAmount(String input,String column) {
        Repository repo = new ParticipantRepository();

        try {
            if (column.equals("phase_title") ||
                    column.equals("username") ||
                    column.equals("participant_name") ||
                    column.equals("position")) {
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

    /*
    // Two methods for insure ', " and \ doesn't create an error
    public String secureInputToDb(String input) {

        boolean stillAnIssue = input.contains("\"") && !input.contains("\\\"") ||
                                input.contains("'") && !input.contains("\\'") ||
                                input.contains("\\") && !input.contains("\\\\");

        System.out.println(input);

        while (stillAnIssue) {
            if (input.contains("\"") && !input.contains("\\\"")) {
                String[] partsOfInput = input.split("\"");
                input = createNewInput(0,partsOfInput.length,input) + "\\" +
                        createNewInput(partsOfInput.length,input.length(),input);

                System.out.println(input);
            }
            else if (input.contains("'") && !input.contains("\\'")) {
                String[] partsOfInput = input.split("'");
                input = createNewInput(0,partsOfInput.length,input) + "\\" +
                        createNewInput(partsOfInput.length,input.length(),input);

                System.out.println(input);
            }
            else if (input.contains("\\") && !input.contains("\\\\")) {
                input = createNewInput(0,input.indexOf("\\"),input) + "\\" +
                        createNewInput(input.indexOf("\\"),input.length(),input);
                System.out.println(input);
            }
            else {
                stillAnIssue = false;
            }
            System.out.println(input);
        }
        return input;
    }
    public String insureInputFromDb(String input) {

        if (input.contains("\\\"") || input.contains("\\'") || input.contains("\\\\")) {
            String stringToEscape = new String();
            if (input.contains("\\\"")) {
                stringToEscape = "\\\"";
            }
            else if (input.contains("\\'")) {
                stringToEscape = "\\'";
            }
            else if (input.contains("\\\\")) {
                stringToEscape = "\\\\";
            }
            input = createNewInput(0,input.indexOf(stringToEscape)-1,input)
                    + createNewInput(input.indexOf(stringToEscape),input.length(),input);
        }

        return input;
    }
    private String createNewInput(int startOfString, int endOfString, String input) {
        char currentLetter = '.';
        String newInput = new String();

        for (int i = startOfString; i < endOfString; i++) {
            currentLetter = input.charAt(i);
            newInput += currentLetter;
        }

        return newInput;
    }

     */

}
