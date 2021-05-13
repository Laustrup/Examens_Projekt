package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectRepository extends Repository{

    private Project project;
    private Task task;
    private Assignment assignment;
    private Phase phase;
    private Participant participant;
    private ProjectManager projectManager;
    private Department department;

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

    public Project findProject(String projectTitle) {

        ResultSet res = executeQuery("SELECT project.project_id, project.title, project_password, project.projectmanager_id, " +
                "projectmanager.username, " +
                "phase_table.phase_id, phase_table.phase_title, " +
                "assignment.assignment_id, assignment.assignment_title, assignment.phase_id, assignment.assignment_start, assignment.assignment_end, assignment.is_completed, " +
                "task.estimated_work_hours, task.task_id, " +
                "participant.participant_id, participant.participant_name, participant.participant_password, participant.position, " +
                "department.department_no, department.location, department.department_name " +
                "FROM project " +
                "INNER JOIN phase_table " +
                "INNER JOIN assignment " +
                "INNER JOIN task " +
                "INNER JOIN participant " +
                "INNER JOIN department " +
                "INNER JOIN projectmanager " +
                "WHERE project.title = '" + projectTitle +  "';");

        // Local variables to be edited from db's values
        ArrayList<Phase> listOfPhases = new ArrayList<>();
        ArrayList<Participant> listOfParticipants = new ArrayList<>();
        ArrayList<Task> listOfTasks = new ArrayList<>();

        /*
        // Objects
        Task task = new Task(0);
        Assignment assignment = new Assignment(new String(),new String(),new String(),false,new ArrayList<>(),new ArrayList<>());
        Phase phase = new Phase(new String());
        Participant participant = new Participant(0,new String());
        ProjectManager projectManager = new ProjectManager(new String(),new String());
        Department department = new Department(new String(),new String(),0);

         */

        // Ints
        int taskId = 0;
        int assignmentId = 0;
        int phaseId = 0;
        int participantId = 0;
        int projectManagerId = 0;
        int projectId = 0;
        int departmentNo = 0;

        int[] ids = {taskId,assignmentId,phaseId,participantId,projectManagerId,projectId, departmentNo};

        // Strings
        String dbProjectTitle = new String();
        String assignmentTitle = new String();
        String phaseTitle = new String();
        String participantName = new String();
        String participantPosition = new String();

        String projectPassword = new String();
        String participantPassword = new String();

        String assignmentStart = new String();
        String assignmentEnd = new String();

        String departmentLocation = new String();
        String departmentName = new String();

        String[] strings = {dbProjectTitle,assignmentTitle,phaseTitle,participantName,participantPosition,
                            projectPassword,participantPassword,assignmentStart,assignmentEnd,departmentLocation,departmentName};

        // Boolean and double
        boolean isCompleted = false;

        double workHours = 0;

        // Maps
        Map<String, Participant> mapOfParticipant = new HashMap<>();
        Map<String, Assignment> mapOfAssignments = new HashMap<>();

        try {
            while (res.next()) {

                // Sets first values
                if (res.isFirst()) {
                    updateObjects(ids, strings, isCompleted, workHours, listOfParticipants, listOfTasks, res);
                }

                //TODO should not be void, needs to return arrays and maps...
                putInObjects();

                // Updates everything
                if (!(res.isFirst())) {
                    updateObjects(ids, strings, isCompleted, workHours, listOfParticipants, listOfTasks, res);
                }

                // Constructs project
                if (res.isLast()) {
                    putInObjects();
                    project = new Project(res.getString("title"),res.getString("password"),
                            listOfPhases,mapOfParticipant, projectManager);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create project...\n" + e.getMessage());
        }

        return project;
    }

    private void updateObjects(int[] ids, String[] strings, boolean isCompleted, double workHours,
                               ArrayList<Participant> listOfParticipants, ArrayList<Task> listOfTasks, ResultSet res) {
        try {
            // Updates values
            strings = updateStrings(strings,res);
            ids = updateIds(ids,res);

            isCompleted = res.getBoolean("is_completed");

            workHours = res.getDouble("estimated_work_hours");

            // Puts values into objects
            task = new Task(workHours);
            assignment = new Assignment(strings[7],strings[8],strings[1],isCompleted,listOfParticipants,listOfTasks);
            phase = new Phase(strings[2]);
            department = new Department(ids[6],strings[9],strings[10]);

            // Checks if participant is projectmanager
            Integer dbParticipantId = ids[3];
            if (dbParticipantId == null) {
                projectManager = new ProjectManager(res.getString("username"),strings[6],ids[4],strings[3],strings[4],department);
            }
            else {
                participant = new Participant(ids[4],strings[6],strings[3],strings[4],department);
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't update objects...\n" + e.getMessage());
        }

    }

    private int[] updateIds(int[] ids, ResultSet res) {

        try {
            ids[0] = res.getInt("task.task_id");
            ids[1] = res.getInt("assignment_id");
            ids[2] = res.getInt("phase_id");
            ids[3] = res.getInt("participant_id");
            ids[4] = res.getInt("projectmanager_id");
            ids[5] = res.getInt("project_id");
            ids[6] = res.getInt("department_no");
        }
        catch (Exception e) {
            System.out.println("Couldn't update Ids...\n" + e.getMessage());
        }

        return ids;
    }

    private String[] updateStrings(String[] strings, ResultSet res) {

        try {
            strings[0] = res.getString("title");
            strings[1] = res.getString("assignment_title");
            strings[2] = res.getString("phase_title");
            strings[3] = res.getString("participant_title");
            strings[4] = res.getString("position");

            strings[5] = res.getString("project_password");
            strings[6] = res.getString("participant_password");

            strings[7] = res.getString("assignment_start");
            strings[8] = res.getString("assignment_end");

            strings[9] = res.getString("location");
            strings[10] = res.getString("department_name");

        }
        catch (Exception e) {
            System.out.println("Couldn't update strings...\n" + e.getMessage());
        }

        return strings;
    }

    private void putInObjects(int[] ids, String[] strings, ResultSet res, int previousId,ArrayList<Phase> listOfPhases,
                              ArrayList<Participant> listOfParticipants,ArrayList<Task>) {
        // Adds Tasks to listOfTasks
        try {
            phase.putInAssignments(String.valueOf(res.getInt("phase_id")),
                    new Assignment(res.getString("assignment_start"),res.getString("assignment_end"),
                            res.getString("assignment.assignment_title"),res.getBoolean("assignment.is_completed"),
                            listOfParticipants,listOfTasks));
            if (res.getInt("assignment.assignment_id") != previousId) {
                listOfPhases.add(phase);
            }
        }
        catch (Exception e) {
            System.out.println();
        }

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
