package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.services.ExceptionHandler;

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

    // Maps
    private Map<String, Participant> mapOfParticipants = new HashMap<>();
    private Map<String, Assignment> mapOfAssignments = new HashMap<>();

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project(title, projectmanager_id) VALUES (\""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
    }

    public Project putProjectInDatabaseWithReturn(Project projectToInsert, int projectmanagerId, ProjectManager projectManager) {
        executeSQLStatement("insert into project values (default, \""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
        ResultSet res = executeQuery("SELECT * FROM project WHERE title = \"" + projectToInsert.getTitle() + "\";");

        try {
            project = new Project(res.getString("title"), new ArrayList<Phase>(), new HashMap<>(), projectManager);
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

    public void putAssignmentInDatabase(Assignment assignment, int phaseId) {

        executeSQLStatement("INSERT INTO assignment VALUES (default, "  + assignment.getStart() +
                            ", " + assignment.getEnd() + ", " + assignment.isCompleted() + ", " +
                             phaseId + "); ");
    }

    public void putTaskInDatabase(int assignmentId) {
        executeSQLStatement("INSERT INTO task VALUES (" + assignmentId + ", " + null + "); ");
    }

    public Project findProject(String projectTitle) {

        ResultSet res = executeQuery("SELECT project.project_id, project.title, project.projectmanager_id, " +
                "projectmanager.username, projectmanager.participant_id, " +
                "phase_table.phase_id, phase_table.phase_title, " +
                "assignment.assignment_id, assignment.assignment_title, assignment.phase_id, assignment.assignment_start, assignment.assignment_end, assignment.is_completed, " +
                "task.estimated_work_hours, task.task_id, " +
                "participant.participant_id, participant.user_id, participant.participant_name, participant.participant_password, participant.position, " +
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
        int projectManagerId = 0;
        int projectId = 0;
        int departmentNo = 0;

        int[] ids = {taskId,assignmentId,phaseId,projectManagerId,projectId, departmentNo};

        // Strings
        String dbProjectTitle = new String();
        String assignmentTitle = new String();
        String phaseTitle = new String();
        String participantName = new String();
        String participantId = new String();
        String participantPosition = new String();

        String participantPassword = new String();

        String assignmentStart = new String();
        String assignmentEnd = new String();

        String departmentLocation = new String();
        String departmentName = new String();

        String[] strings = {dbProjectTitle,assignmentTitle,phaseTitle,participantName,participantId,participantPosition,
                            participantPassword,assignmentStart,assignmentEnd,departmentLocation,departmentName};

        // Boolean and double
        boolean isCompleted = false;

        double workHours = 0;

        try {

            while (res.next()) {

                // Sets first values
                if (res.isFirst()) {
                    updateObjects(ids, strings, isCompleted, workHours, listOfParticipants, listOfTasks, res);
                }

                listOfPhases = addTolistOfPhases(ids,strings,listOfPhases);

                // Updates everything
                if (!(res.isFirst())) {
                    updateObjects(ids, strings, isCompleted, workHours, listOfParticipants, listOfTasks, res);
                }

                // Constructs project
                if (res.isLast()) {
                    project = new Project(strings[0],listOfPhases,mapOfParticipants, projectManager);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create project...\n" + e.getMessage());
        }

        return project;
    }

    // updates all objects to current values of the dbrow, except for phase
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
            mapOfAssignments.put(String.valueOf(ids[1]),assignment);
            department = new Department(ids[5],strings[9],strings[10]);

            // Checks if participant is projectmanager
            int projectmanagersParticipantId = res.getInt(5);
            int participantId = res.getInt(17);

            if (projectmanagersParticipantId == participantId) {
                projectManager = new ProjectManager(res.getString("username"),strings[6],
                                                    strings[5],strings[3],strings[4],department);
                if (!(mapOfParticipants.containsKey(ids[3]))) {
                mapOfParticipants.put(String.valueOf(ids[3]),projectManager);
                }
            }
            else {
                participant = new Participant(strings[5],strings[6],strings[3],strings[4],department);
                if (!(mapOfParticipants.containsKey(strings[5]))) {
                    mapOfParticipants.put(strings[5],participant);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't update objects...\n" + e.getMessage());
        }

    }

    private int[] updateIds(int[] ids, ResultSet res) {

        try {
            ids[0] = res.getInt("task_id");
            ids[1] = res.getInt("assignment_id");
            ids[2] = res.getInt("phase_id");
            ids[3] = res.getInt("projectmanager_id");
            ids[4] = res.getInt("project_id");
            ids[5] = res.getInt("department_no");
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
            strings[3] = res.getString("participant_name");
            strings[4] = res.getString("position");
            strings[5] = res.getString("user_id");

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

    private ArrayList<Phase> addTolistOfPhases(int[] ids, String[] strings, ArrayList<Phase> listOfPhases) {

        try {

            // Phase
            if (ids[2] > ids[5]) {
                phase = new Phase(strings[2],mapOfAssignments);
                listOfPhases.add(phase);
                mapOfAssignments = new HashMap<>();
            }

        }
        catch (Exception e) {
            System.out.println();
        }

        return listOfPhases;
    }

    public void updateProject(Project project,String formerTitle) {

        executeSQLStatement("UPDATE project " +
                "SET title = '" + project.getTitle() + "', " +
                "WHERE projectmanager_username = '" + formerTitle + "';");
    }

}
