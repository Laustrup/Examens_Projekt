package patrick_laust_ayo.examproject.services;

import org.apache.catalina.User;
import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.repositories.ProjectManagerRepository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectCreator {

    private Project project;
    private Phase phase;
    private Assignment assignment;
    private Task task;

    private ProjectRepository projectRepo = new ProjectRepository();
    private ProjectManagerRepository projectManagerRepo = new ProjectManagerRepository();

    public Project createProject(String title, String managerName) {

        UserCreator userCreator = new UserCreator();
        project = new Project(title, new ArrayList<>(), new HashMap<>(), userCreator.getProjectManager(managerName));
        projectManagerRepo.closeCurrentConnection();
        try {
            ResultSet res = projectManagerRepo.findProjectManager(managerName);
            res.next();

            projectRepo.putProjectInDatabase(project, res.getInt("projectmanager_id"));
                //    projectManagerRepo.findProjectManager(managerName).getInt("projectmanager_id"));
        }
        catch (Exception e) {
            System.out.println("Couldn't put project in database...\n" + e.getMessage());
            e.printStackTrace();
        }
        projectManagerRepo.closeCurrentConnection();
        return project;
    }
    public Project getProject(String projectTitle) {

        ResultSet res = projectRepo.findProject(projectTitle);

        // Local variables to be edited from db's values

        // Objects
        Task task = new Task(0,new ArrayList<>());
        Assignment assignment = new Assignment(new String(),new String(),new String(),false,new ArrayList<>());
        Phase phase = new Phase(new String());
        Participant participant = new Participant(new String(),new String());
        ProjectManager projectManager = new ProjectManager(new String(),new String());
        Department department = new Department(0,new String(),new String());

        // ArrayLists
        ArrayList<Phase> listOfPhases = new ArrayList<>();
        ArrayList<Participant> listOfParticipants = new ArrayList<>();
        ArrayList<Task> listOfTasks = new ArrayList<>();

        // Maps for project
        Map<String, Participant> mapOfParticipants = new HashMap<>();
        Map<String, Assignment> mapOfAssignments = new HashMap<>();

        Object[] objects = {task,assignment,phase,participant,projectManager,
                department,mapOfParticipants,mapOfAssignments,
                listOfPhases, listOfParticipants, listOfTasks};

        // Ints
        int taskId = 0;
        int assignmentId = 0;
        int phaseId = 0;
        int projectManagerId = 0;
        int projectId = 0;
        int departmentNo = 0;
        int currentProjectMember = 0;
        int participantId = 0;
        int foreignParticipantId = 0;

        int[] currentIds = {taskId,assignmentId,phaseId,projectManagerId,projectId, departmentNo,
                currentProjectMember,participantId,foreignParticipantId};
        int[] formerIds = {taskId,assignmentId,phaseId,projectManagerId,projectId, departmentNo,
                currentProjectMember,participantId,foreignParticipantId};

        // Strings
        String dbProjectTitle = new String();
        String assignmentTitle = new String();
        String phaseTitle = new String();
        String participantName = new String();
        String userId = new String();
        String participantPosition = new String();

        String participantPassword = new String();

        String assignmentStart = new String();
        String assignmentEnd = new String();

        String departmentLocation = new String();
        String departmentName = new String();

        String[] strings = {dbProjectTitle,assignmentTitle,phaseTitle,participantName,userId,participantPosition,
                participantPassword,assignmentStart,assignmentEnd,departmentLocation,departmentName};

        // Boolean and double
        boolean isCompleted = false;
        double workHours = 0;

        try {
            while (res.next()) {
                // In case there is only one row
                // TODO Will there ever be only one row?
                if (res.isLast() && res.isFirst()) {
                    project = new Project(res.getString("title"));
                    projectManager = new UserCreator().getProjectManager(res.getString("username"));
                    project.setProjectManager(projectManager);
                    mapOfParticipants.put("Projectmember number " + 0,projectManager);
                    project.setParticipants(mapOfParticipants);
                    break;
                }

                // Updates values
                currentIds = updateIds(currentIds,res);
                // TODO If anything is wrong, it should the order of ifs in updateObjects
                if (!res.isFirst()) {
                    objects =  updateObjects(objects,currentIds, formerIds, strings, res,workHours,isCompleted);
                }

                // Updates values of former row
                formerIds = updateIds(formerIds,res);
                strings = updateStrings(strings,res);
                isCompleted = res.getBoolean("is_completed");
                workHours = res.getDouble("estimated_work_hours");


                if (res.isLast()) {
                    objects =  updateObjects(objects,currentIds, formerIds, strings, res,workHours,isCompleted);
                    project = new Project(strings[0],(ArrayList<Phase>) objects[8],
                            (HashMap<String, Participant>)objects[6],(ProjectManager) objects[4]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create project...\n" + e.getMessage());
            e.printStackTrace();
            project = null;
        }
        projectRepo.closeCurrentConnection();
        return project;
    }
    // updates all objects to current values of the dbrow, except for phase
    private Object[] updateObjects(Object[] objects, int[] currentIds, int[] formerIds, String[] strings,
                                   ResultSet res, double workHours,boolean isCompleted) {
        try {

            // Checks if participant is projectmanager
            if (currentIds[3]>formerIds[3] || currentIds[6]>formerIds[6] || res.isLast()) {
                objects[5] = new Department(formerIds[5],strings[9],strings[10]);
                if (formerIds[6] == formerIds[7]) {
                    objects[4] = new ProjectManager(res.getString("username"),strings[6],
                            strings[5],strings[3],strings[4],(Department) objects[5]);
                    ((Map<String, Participant>)objects[6]).put(((ProjectManager)objects[4]).getId(),(ProjectManager) objects[4]);
                    ((ArrayList<Participant>) objects[9]).add((ProjectManager) objects[4]);
                }
                else {
                    objects[3] = new Participant(strings[5],strings[6],strings[3],strings[4],(Department) objects[5]);
                    ((Map<String, Participant>)objects[6]).put(((Participant) objects[3]).getId(),(Participant) objects[3]);
                    ((ArrayList<Participant>) objects[9]).add((Participant) objects[3]);
                }

                // Assignment
                if (currentIds[1]>formerIds[1] || res.isLast()) {
                    objects[1] = new Assignment(strings[7],strings[8],strings[1],isCompleted,(ArrayList<Task>) objects[10]);
                    ((HashMap<String, Assignment>)objects[7]).put(String.valueOf(formerIds[1]),(Assignment) objects[1]);
                }

                // Phase
                if (currentIds[2] > formerIds[2] || res.isLast()) {
                    objects[2] = new Phase(strings[2],(Map<String,Assignment>)objects[7]);
                    objects[7] = new HashMap<String, Assignment>();
                    ((ArrayList<Phase>)objects[8]).add((Phase)objects[2]);
                }

                // Task
                if (currentIds[0]>formerIds[0] || res.isLast()){
                    objects[0] = new Task(workHours,(ArrayList<Participant>) objects[9]);
                    ((ArrayList<Task>)objects[10]).add((Task)objects[0]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't update objects...\n" + e.getMessage());
        }
        return objects;
    }
    private int[] updateIds(int[] ids, ResultSet res) {
        try {
            ids[0] = res.getInt("task_id");
            ids[1] = res.getInt("assignment_id");
            ids[2] = res.getInt("phase_id");
            ids[3] = res.getInt("projectmanager_id");
            ids[4] = res.getInt("project_id");
            ids[5] = res.getInt("department_no");
            ids[6] = res.getInt("participant.participant_id");
            ids[7] = res.getInt("projectmanager.participant_id");
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
    public ArrayList<Project> getProjects(String userId) {
        ResultSet res = projectRepo.findProjects(userId);
        ArrayList<Project> projects = new ArrayList<>();

        int currentProjectId;
        int formerProjectId = 0;

        try {
            while (res.next()) {
                // If there is only one row
                if (res.isFirst() && res.isLast()) {
                    projects.add(getProject(res.getString("title")));
                    break;
                }
                // Otherwise it compares former- and current ids, and as well adds the last project
                currentProjectId = res.getInt("project_id");
                if (currentProjectId > formerProjectId && !res.isFirst()) {
                    projects.add(getProject(res.getString("title")));
                }
                formerProjectId = res.getInt("project_id");
                if (res.isLast()) {
                    projects.add(getProject(res.getString("title")));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't gather projects...\n" + e.getMessage());
        }
        return projects;
    }

    public Phase createPhase(String projectTitle) {

        phase = new Phase(new String());

        int id = projectRepo.findId("project","title",projectTitle, "project_id");
        projectRepo.putPhaseInDatabase(id);

        return phase;
    }
    public Phase getPhase(String phaseTitle,String projectTitle) {
        ResultSet res = projectRepo.findPhase(phaseTitle,projectTitle);

        Phase phase = new Phase(new String(),new HashMap<>());
        Map<String, Assignment> assignments = new HashMap<>();
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Participant> participants = new ArrayList<>();
        Department department = new Department(0,new String(), new String());

        int formerAssignmentId = 0;
        int formerTaskId = 0;
        int formerParticipantId = 0;
        int formerDepartmentId = 0;

        try {
            while (res.next()) {
                // If there is only one row
                if (res.isFirst() && res.isLast()) {
                    phase = new Phase(res.getString("phase_title"),assignments);
                    break;
                }

                // Otherwise it continues to put in all values, and in the end constructs the object
                int currentAssignmentId = res.getInt("assignment_id");
                int currentTaskId = res.getInt("task_id");
                int currentParticipantId = res.getInt("participant_id");
                int currentDepartmentId = res.getInt("department_no");

                if (currentDepartmentId > formerDepartmentId) {
                    department = new Department(currentDepartmentId,res.getString("location"),res.getString("department_name"));
                }
                if (currentParticipantId > formerParticipantId && !res.isFirst()) {
                    participants.add(new Participant(res.getString("user_id"),res.getString("participant_password"),
                            res.getString("participant_name"),res.getString("position"),department));
                }
                if (currentTaskId > formerTaskId && !res.isFirst()) {
                    tasks.add(new Task(res.getDouble("estimated_work_hours"),participants));
                }
                if (currentAssignmentId > formerAssignmentId && !res.isFirst()) {
                    assignments.put(String.valueOf(res.getInt("assignment_id")),
                            new Assignment(res.getString("assignment_start"), res.getString("assignment_end"),
                                    res.getString("assignment_title"),res.getBoolean("is_completed"),
                                    tasks));
                }

                formerAssignmentId = res.getInt("assignment_id");
                formerTaskId = res.getInt("task_id");
                formerParticipantId = res.getInt("participant_id");
                formerDepartmentId = res.getInt("department_no");

                if (res.isLast()) {
                    assignments.put(String.valueOf(res.getInt("assignment_id")),
                            new Assignment(res.getString("assignment_start"), res.getString("assignment_end"),
                                    res.getString("assignment_title"),res.getBoolean("is_completed"),
                                    tasks));
                    phase = new Phase(res.getString("phase_title"),assignments);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create phase from database...\n" + e.getMessage());
            phase = null;
        }

        return phase;

    }

    public Assignment createAssignment(String phaseTitle, String start, String end) {

        assignment = new Assignment(start,end,new String(),false, new ArrayList<Task>());

        int phaseId = projectRepo.findId("phase_table","phase_title",phaseTitle,"phase_id");
        Integer taskId = null;

        projectRepo.putAssignmentInDatabase(assignment,phaseId);

        return assignment;
    }

    public Task createTask(String assignmentTitle, Participant participant) {

        Double workHours = null;

        task = new Task(workHours,new ArrayList<>());
        task.addParticipant(participant);

        projectRepo.putTaskInDatabase(projectRepo.findId("assignment","assignment_title",assignmentTitle,"assignment_id"));

        return task;
    }


}
