package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public boolean allowLogin(String password) {
        ParticipantRepository repo = new ParticipantRepository();

        ResultSet res = repo.selectAll("participant");

        try {
            while (res.next()) {
                if (res.getString("participant_password").equals(password)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Trouble identifying ResultSet when searching user_id...\n" + e.getMessage());
        }
        return false;
    }
    public boolean isProjectFullybooked(Project project) {
        for (int i = 0; i < project.getParticipants().size(); i++) {
            Participant participant = project.getParticipants().get("Projectmember number " + i);
            if (participant.getId() == null || participant.getPassword() == null) {
                return false;
            }
        }
        return true;
    }

    // TODO Perhaps unmake repository as abstract?
    // Accepts if the participant is part of the project
    public boolean isParticipantPartOfProject(String userId, String projectTitle) {

        Repository repo = new ParticipantRepository();

        int participantId = repo.findId("participant", "user_id", userId,"partipant_id");
        int projectId = repo.findId("project", "title", projectTitle,"project_id");

        ResultSet res = repo.executeQuery("SELECT * FROM participant_project WHERE participant_id = " + participantId + ";");
        repo.closeCurrentConnection();

        try {
            if (res.getInt("project_id") == projectId) {
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Trouble comparing ids when checking if participant part of project...\n" + e.getMessage());
        }
        return false;
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
