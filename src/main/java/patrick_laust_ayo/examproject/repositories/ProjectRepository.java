package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectRepository extends Repository{

    private Project project;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project VALUES (DEFAULT, \""  + projectToInsert.getTitle() + "\", \"" +
                projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
    }

    public Project putProjectInDatabaseWithReturn(Project projectToInsert, int projectmanagerId, ProjectManager projectManager) {
        executeSQLStatement("insert into project values (default, \""  + projectToInsert.getTitle() + "\", \"" +
                                            projectToInsert.getPassword() + "\", " + projectmanagerId + "); ");
        ResultSet res = executeQuery("SELECT * FROM project WHERE title = \"" + projectToInsert.getTitle() + "\";");

        try {
            project = new Project(res.getString("title"), res.getString("project_password"),
                      new ArrayList<Phase>(), new HashMap<>(), projectManager);
        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            project = null;
        }

        return project;
    }

    public void putPhaseInDatabase(int projectmanagerId) {
        executeSQLStatement("INSERT INTO phase VALUES (default, "  + null + ", " + projectmanagerId + "); ");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId, int participantId, int taskId) {

        executeSQLStatement("INSERT INTO assignment VALUES (default, "  + assignment.getStart() +
                            ", " + assignment.getEnd() + ", " + assignment.isCompleted() + ", " +
                             phaseId + ", " + participantId + ", " + taskId + "); ");
    }

    public Project findProject(String projectTitle, ProjectManager projectManager) {

        ResultSet res = executeQuery("SELECT project.project_id, project.title, project_password, projectmanager_id, " +
                "phase_table.phase_id, phase_table.phase_title, " +
                "assignment.assignment_id, assignment.assignment_title, assignment.phase_id, assignment.assignment_start, assignment.assignment_end, assignment.is_completed, " +
                "task.estimated_work_hours, " +
                "participant.participant_id, participant.participant_password " +
                "FROM project " +
                "INNER JOIN phase_table " +
                "INNER JOIN assignment " +
                "INNER JOIN task " +
                "INNER JOIN participant " +
                "WHERE project.title = " + projectTitle +  ";");

        // Local variables to be edited from db's values
        ArrayList<Phase> listOfPhases = new ArrayList<>();
        ArrayList<Participant> listOfParticipants = new ArrayList<>();
        ArrayList<Task> listOfTasks = new ArrayList<>();

        Phase phase = new Phase(new String());
        Assignment assignment = new Assignment(new String(),new String(),new String(),false,new ArrayList<>(),new ArrayList<>());
        Task task = new Task(0);
        Participant participant = new Participant(0,new String());

        Object[] objects = {phase,assignment,task,participant,projectManager};

        int phaseId = 0;
        int participantId = 0;
        int assignmentId = 0;
        int projectManagerId = 0;
        int projectId = 0;

        int[] ints = {phaseId,participantId,assignmentId,projectManagerId,projectId};

        String dbProjectTitle = new String();
        String phaseTitle = new String();
        String assignmentTitle = new String();

        String projectPasswprd = new String();
        String participantPassword = new String();

        String assignmentStart = new String();
        String assignmentEnd = new String();

        String[] strings = {dbProjectTitle,phaseTitle,assignmentTitle,projectPasswprd,participantPassword,assignmentStart,assignmentEnd};

        boolean isCompleted = false;

        double workHours = 0;

        Map<String, Participant> mapOfParticipant = new HashMap<>();
        Map<String, Assignment> mapOfAssignments = new HashMap<>();

        try {
            while (res.next()) {

                // Sets first values
                if (res.isFirst()) {
                    objects = setFirstValues(objects,ints,strings,isCompleted,workHours,mapOfParticipant,mapOfAssignments);
                    previousId = res.getInt("phase_table.phase_id");
                }

                //
                if (res.getInt("assignment.phase_id") != previousId) {
                    listOfPhases.add(phase);
                }

                phase.putInAssignments(String.valueOf(res.getInt("phase_id")),
                                        new Assignment(res.getString("assignment_start"),res.getString("assignment_end"),
                                                        res.getString("assignment.assignment_title"),res.getBoolean("assignment.is_completed"),
                                                        listOfParticipants,listOfTasks));
                phase.setTitle("title");
                previousId = res.getInt("phase_table.phase_id");
                previousProjectTitle = res.getString("title");

                if (res.isLast()) {

                    Project project = new Project(res.getString("title"),res.getString("password"),
                            listOfPhases,mapOfParticipant, projectManager);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create project...\n" + e.getMessage());
        }

        return project;
    }

    private Object[] setFirstValues(Object[] objects, int[] ints, String strings,
                                    boolean isCompleted,double workHours,
                                    Map<String, Participant> mapOfParticipant, Map<String, Assignment> assignments) {

    }

    public boolean doesProjectExist(String title){
        ResultSet res = executeQuery("SELECT * FROM project WHERE title = " + title + ";");

        try {
            while (res.next()) {
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't look through resultSet...\n" + e.getMessage());
        }

        return false;

    }

}
