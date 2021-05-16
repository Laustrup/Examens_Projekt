package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.repositories.ParticipantRepository;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.Repository;

import java.sql.ResultSet;

public class ExceptionHandler {

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
        ResultSet res = repo.SelectAll(table);
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

    // TODO Needs to finnish typing in limit values
    public boolean inputMatchesDbLength(String input, String table, String column) {

        try {
            if (noRoomForNewId(table, column) || columnsPermittingTwentyFive(input, table, column)) {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Column title doesn't include '_'...\n" + e.getMessage());
        }

        return true;

    }

    private boolean noRoomForNewId(String table, String column) throws Exception {
        Repository repo = new ParticipantRepository();
        String[] titleOfColumn;

        try {
            titleOfColumn = column.split("_");

            if (titleOfColumn[1].equals("id") || column.equals("id")) {
                if (repo.calcNextId(table)>255) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            throw new Exception();
        }
        return false;
    }

    private boolean columnsPermittingTwentyFive(String input, String table, String column) throws Exception {
        Repository repo = new ParticipantRepository();
        String[] titleOfColumn;

        try {
            titleOfColumn = column.split("_");
            if (titleOfColumn[1].equals("title") || titleOfColumn[1].equals("name") && !titleOfColumn[1].equals("department") ||
                    titleOfColumn[1].equals("password") || ) {
                if (input.length()>25) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            throw new Exception();
        }
        return false;
    }

    public boolean doesProjectExist(String title){

        ProjectRepository repo = new ProjectRepository();

        if (repo.findProject(title) == null) {
            return false;
        }
        else {
            return true;
        }
    }
}
