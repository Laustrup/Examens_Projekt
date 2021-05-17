package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;
import patrick_laust_ayo.examproject.repositories.Repository;

import java.sql.ResultSet;

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

        ProjectRepository repo = new ProjectRepository();

        if (repo.findProject(title) == null) {
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

    public boolean isParticipantPartOfProject(String user, Project project) {

        ParticipantRepository repo = new ParticipantRepository();

        repo.findParticipant()

        return false;
    }


    // Checks if input is too long and writes a message as return, if input is allowed, it returns "Input is allowed"
    public String isLengthAllowedInDatabase(String input, String table, String column)  {

        if (inputAsTitleIsTooLongByAmount(input, table, column) != -1) {
            return "Title is too long... Write less than " + inputAsTitleIsTooLongByAmount(input, table, column) + " words!";
        }
        if (input.equals("participant_password") && input.length()>25) {
            return "Password is too long... Write less than 25 words!";
        }
        if (input.equals("user_id") && input.length()>15) {
            return "ID is too long... Write less than 15 words!";
        }

        return "Input is allowed";

    }
    private int inputAsTitleIsTooLongByAmount(String input, String table, String column) {
        Repository repo = new ParticipantRepository();

        try {
            if (input.equals("phase_title") ||
                    input.equals("username") ||
                    input.equals("participant_name") ||
                    input.equals("participant_password") ||
                    input.equals("position")) {
                if (input.length()>25) {
                    return 25;
                }
            }
            if (input.equals("title") ||
                input.equals("department_name")) {
                if (input.length()>30) {
                    return 30;
                }
            }
            if (input.equals("assignment_title") && input.length()>50) {
                return 50;
            }
            if (input.equals("location") && input.length()>100) {
                return 100;
            }
        }
        catch (Exception e) {
            System.out.println("Input is too long...");
        }
        return -1;
    }

    // Two methods for insure ', " and \ doesn't create an error
    public String stringInputToDbInsure(String input) {
        if (input.endsWith("\"") || input.endsWith("'") || input.endsWith("\\")) {
            input += "Д";
        }
        if (input.startsWith("\"") || input.startsWith("'") || input.startsWith("\\")) {
            String newInput = "Д" + input;
            input = newInput;
        }
        return input;
    }
    public String stringInputFromDbInsure(String input) {

        String Д = "Д";

        if (input.startsWith(Д) && input.endsWith(Д)) {
            input = createNewInput(1,input.length()-1,input);
        }
        else if (input.startsWith(Д) && !(input.endsWith(Д))) {
            input = createNewInput(1,input.length(),input);
        }
        else if (input.endsWith(Д) && !(input.startsWith(Д))) {
            input = createNewInput(0,input.length()-1,input);
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

}
