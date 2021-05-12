package patrick_laust_ayo.examproject.services;

import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.ProjectManager;

public class UserEditor {

    private ProjectManager projectManager;
    private Participant participant;

    public boolean isInputInteger(String input) {
        try {
            int parsedInput = Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
