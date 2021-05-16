package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.repositories.ProjectRepository;

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
