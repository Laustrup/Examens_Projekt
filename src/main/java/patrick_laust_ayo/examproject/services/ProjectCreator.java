package patrick_laust_ayo.examproject.services;

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

    private String[] updateStrings(String[] strings, ResultSet res) {
        try {
            // Department
            strings[0] = res.getString("location");
            strings[1] = res.getString("department_name");

            // Participant
            strings[2] = res.getString("user_id");
            strings[3] = res.getString("participant_password");
            strings[4] = res.getString("participant_name");
            strings[5] = res.getString("position");;

            // Task
            strings[6] = res.getString("task_start");
            strings[7] = res.getString("task_end");
            strings[8] = res.getString("task_title");

            // Assignment
            strings[9] = res.getString("assignment_start");
            strings[10] = res.getString("assignment_end");
            strings[11] = res.getString("assignment_title");

            // Project
            strings[12] = res.getString("title");

            // Phase
            strings[13] = res.getString("phase_title");

            // ProjectManager
            strings[15] = res.getString("username");
        }
        catch (Exception e) {
            System.out.println("Couldn't update strings...\n" + e.getMessage());
            e.printStackTrace();
        }
        return strings;
    }

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

        // ArrayLists
        ArrayList<Phase> listOfPhases = new ArrayList<>();
        ArrayList<Participant> listOfParticipants = new ArrayList<>();
        ArrayList<Task> listOfTasks = new ArrayList<>();

        // Maps for project
        Map<String, Participant> mapOfParticipants = new HashMap<>();
        Map<String, Assignment> mapOfAssignments = new HashMap<>();

        // Ints
        int currentTaskId = 0;
        int currentAssignmentId = 0;
        int currentPhaseId = 0;
        int currentParticipantId = 0;

        int formerTaskId = 0;
        int formerAssignmentId = 0;
        int formerPhaseId = 0;
        int formerParticipantId = 0;
        int assignmentMapKey = 0;

        int departmentId = 0;

        String[] strings = new String[16];

        // Boolean and double
        boolean assignmentIsCompleted = false;
        boolean taskIsCompleted = false;
        boolean isProjectManagerCreated = false;

        double workHours = 0;

        ProjectManager projectManager = new ProjectManager(new String(),new String());

        try {
            while (res.next()) {
                // In case there is only one row
                if (res.isLast() && res.isFirst()) {
                    project = new Project(res.getString("title"));
                    projectManager = new UserCreator().getProjectManager(res.getString("username"));
                    project.setProjectManager(projectManager);
                    mapOfParticipants.put(projectManager.getId(), projectManager);
                    project.setParticipants(mapOfParticipants);
                    break;
                }

                // Updates values
                if (!res.isFirst()) {
                    // Ids
                    currentTaskId = res.getInt("task_id");
                    currentAssignmentId = res.getInt("assignment_id");
                    currentPhaseId = res.getInt("phase_id");
                    currentParticipantId = res.getInt("participant_id");

                    // projectManager
                    if (!isProjectManagerCreated && (strings[15].equals(strings[2]))) {
                        projectManager = new ProjectManager(strings[15],strings[3], strings[2],strings[4],strings[5],
                                new Department(departmentId,strings[0],strings[1]));
                        mapOfParticipants.put(projectManager.getId(), projectManager);
                        listOfParticipants.add(projectManager);
                        isProjectManagerCreated = true;
                    }
                    // Participant
                    else if (currentTaskId > formerTaskId || currentParticipantId > formerParticipantId) {
                        Participant participant = new Participant(strings[2], strings[3], strings[4], strings[5],
                                new Department(departmentId,strings[0],strings[1]));
                        mapOfParticipants.put(participant.getId(), participant);
                        listOfParticipants.add(participant);
                    }

                    // Task
                    if (currentTaskId>formerTaskId){
                        listOfTasks.add(new Task(workHours,listOfParticipants,strings[6],strings[7], strings[8],taskIsCompleted));
                        listOfParticipants = new ArrayList<>();
                    }

                    // Assignment
                    if (currentAssignmentId>formerAssignmentId) {
                        assignment = new Assignment(strings[9],strings[10],strings[11],assignmentIsCompleted,listOfTasks);
                        listOfTasks = new ArrayList<>();
                        mapOfAssignments.put(assignment.getTitle(), assignment);
                        mapOfAssignments.put(String.valueOf(assignmentMapKey), assignment);
                        assignmentMapKey++;
                    }

                    // Phase
                    if (currentPhaseId>formerPhaseId) {
                        phase = new Phase(strings[13],mapOfAssignments);
                        mapOfAssignments = new HashMap<String, Assignment>();
                        listOfPhases.add(phase);
                    }
                }

                // Updates values of former row
                departmentId = res.getInt("department_no");

                strings = updateStrings(strings,res);
                assignmentIsCompleted = res.getBoolean("is_completed");
                taskIsCompleted = res.getBoolean("task_is_completed");
                workHours = res.getDouble("estimated_work_hours");

                if (res.isLast()) {
                    boolean allowAddTask = true;

                    // If ProjectManager isn't added
                    if (!isProjectManagerCreated) {
                        if (listOfTasks.size() == 0){
                            listOfTasks.add(new Task(workHours,listOfParticipants,strings[6],strings[7], strings[8],taskIsCompleted));
                            allowAddTask = false;
                        }
                        projectManager = new ProjectManager(strings[15],strings[3], strings[2],strings[4],strings[5],
                                new Department(departmentId,strings[0],strings[1]));
                        if (!mapOfParticipants.containsKey(projectManager.getId())) {
                            mapOfParticipants.put(projectManager.getId(), projectManager);
                        }
                        listOfTasks.get(listOfTasks.size()-1).addParticipant(projectManager);
                    }
                    // If Participant isn't added
                    else if (isProjectManagerCreated) {
                        Participant participant = new Participant(strings[2], strings[3], strings[4], strings[5],
                                                    new Department(departmentId,strings[0],strings[1]));
                        if (listOfTasks.size() == 0){
                            listOfTasks.add(new Task(workHours,listOfParticipants,strings[6],strings[7], strings[8],taskIsCompleted));
                            allowAddTask = false;
                        }
                        mapOfParticipants.put(participant.getId(), participant);
                        listOfTasks.get(listOfTasks.size()-1).addParticipant(participant);
                    }

                    // Task
                    if (allowAddTask) {
                        listOfTasks.add(new Task(workHours,listOfParticipants,strings[6],strings[7], strings[8],taskIsCompleted));
                        System.out.println("Tasks size " + listOfTasks.size());
                    }

                    // Assignment
                    assignment = new Assignment(strings[9],strings[10],strings[11],assignmentIsCompleted,listOfTasks);
                    mapOfAssignments.put(assignment.getTitle(),assignment);
                    phase.putInAssignments(assignment.getTitle(),assignment);
                    mapOfAssignments.put(String.valueOf(assignmentMapKey), assignment);


                    // Phase
                    phase = new Phase(strings[13],mapOfAssignments);
                    listOfPhases.add(phase);

                    project = new Project(strings[12],listOfPhases, mapOfParticipants,projectManager);
                }
                formerTaskId = res.getInt("task_id");
                formerAssignmentId = res.getInt("assignment_id");
                formerPhaseId = res.getInt("phase_id");
                formerParticipantId = res.getInt("participant_id");
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create project...\n" + e.getMessage());
            e.printStackTrace();
            project = null;
        }
        projectRepo.closeCurrentConnection();
        System.out.println("ProjectCreator - getProject");
        System.out.println("listOfPhases.size() er: " + listOfPhases.size());
        for (int i = 0; i < listOfPhases.size(); i++){
            System.out.println("Phase Title: " + listOfPhases.get(i).getTitle());
        }
        return project;
    }


    public ArrayList<Project> getProjects(String userId) {
        ResultSet res = projectRepo.findProjects(userId);
        ArrayList<Project> projects = new ArrayList<>();

        int currentProjectId;
        int formerProjectId = 0;
        //TODO denne metode skal testes, tager kun det første projektnavn med
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
            e.printStackTrace();
        }
        for (int i = 0; i < projects.size(); i++){
            System.out.println("Username: " + userId + " | Project: " + projects.get(i).getTitle());
        }
        return projects;
    }

    public Phase createPhase(String projectTitle) {

        phase = new Phase("NEW PHASE");

        int id = projectRepo.findId("project","title",projectTitle, "project_id");
        projectRepo.putPhaseInDatabase(id);

        return phase;
    }
    public Phase getPhase(String phaseTitle,String projectTitle) {
        ResultSet res = projectRepo.findPhase(phaseTitle,projectTitle);

        // Objects
        Phase phase = new Phase(new String(),new HashMap<>());

        // Map and Lists
        Map<String, Assignment> assignments = new HashMap<>();
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Participant> participants = new ArrayList<>();

        String[] strings = new String[16];

        int formerAssignmentId = 0;
        int formerTaskId = 0;
        int formerParticipantId = 0;
        int departmentId = 0;
        int assignmentMapKey = 0;

        double workHours = 0;
        boolean isTaskCompleted = false;
        boolean isAssignmentCompleted = false;

        try {
            while (res.next()) {
                // If there is only one row
                if (res.isFirst() && res.isLast()) {
                    phase = new Phase(res.getString("phase_title"),assignments);
                    break;
                }
                int currentAssignmentId = res.getInt("assignment_id");
                int currentTaskId = res.getInt("task_id");
                int currentParticipantId = res.getInt("participant_id");

                if (!res.isFirst()) {

                    // Participant
                    if (currentParticipantId > formerParticipantId || currentTaskId > formerTaskId) {
                        participants.add(new Participant(strings[2], strings[3], strings[4], strings[5],
                                new Department(departmentId, strings[0], strings[1])));
                    }

                    // Task
                    if (currentTaskId > formerTaskId) {
                        tasks.add(new Task(workHours, participants, strings[6], strings[7], strings[8], isTaskCompleted));
                        participants = new ArrayList<>();
                    }

                    // Assignment
                    if (currentAssignmentId > formerAssignmentId) {
                        assignment = new Assignment(strings[9], strings[10], strings[11], isAssignmentCompleted, tasks);
                        assignments.put(assignment.getTitle(), assignment);
                        assignments.put(String.valueOf(assignmentMapKey), assignment);
                        assignmentMapKey++;
                    }
                }

                departmentId = res.getInt("department_no");
                workHours = res.getDouble("estimated_work_hours");
                isTaskCompleted = res.getBoolean("task_is_completed");
                isAssignmentCompleted = res.getBoolean("is_completed");
                strings = updateStrings(strings,res);


                if (res.isLast()) {
                    // If participant isn't added

                    if (currentParticipantId > formerParticipantId || currentTaskId > formerTaskId) {
                        if(tasks.size() == 0){
                            tasks.add(new Task(workHours, participants,strings[6],strings[7], strings[8], isTaskCompleted));
                        }
                        tasks.get(tasks.size()-1).addParticipant(new Participant(strings[2], strings[3], strings[4], strings[5],
                                new Department(departmentId, strings[0], strings[1])));
                    }

                    // If task isn't added
                    if (!(tasks.get(tasks.size()-1).getStart().equals(strings[6]) &&
                            tasks.get(tasks.size()-1).getEnd().equals(strings[7]) &&
                            tasks.get(tasks.size()-1).getTitle().equals(strings[8]))) {
                        tasks.add(new Task(workHours, participants, strings[6], strings[7], strings[8], isTaskCompleted));
                    }

                    // If assignment isn't added
                    if (!assignments.containsKey(strings[11])) {
                        assignment = new Assignment(strings[9],strings[10],strings[11],isAssignmentCompleted,tasks);
                        assignments.put(assignment.getTitle()+assignment.getStart()+assignment.getEnd(),assignment);
                    }

                    assignments.put(String.valueOf(formerAssignmentId), new Assignment(strings[9], strings[10], strings[11],
                            isAssignmentCompleted, tasks));
                    phase = new Phase(res.getString("phase_title"),assignments);
                    System.out.println(phase.getTitle());
                }
                formerAssignmentId = res.getInt("assignment_id");
                formerTaskId = res.getInt("task_id");
                formerParticipantId = res.getInt("participant_id");
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create phase from database...\n" + e.getMessage());
            e.printStackTrace();
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
    public Assignment getAssignment(String assignmentTitle,String phaseTitle) {
        ResultSet res = projectRepo.findAssignment(assignmentTitle,phaseTitle);
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Participant> participants = new ArrayList<>();
        Department department = new Department(0,new String(), new String());

        int formerTaskId = 0;
        int formerParticipantId = 0;
        int departmentNo = 0;

        String[] strings = new String[16];

        double workHours = 0;
        boolean isTaskCompleted = false;

        try {
            while (res.next()) {
                // If there is only one row
                if (res.isFirst() && res.isLast()) {
                    assignment = new Assignment(res.getString("assignment_start"), res.getString("assignment_end"),
                            res.getString("assignment_title"),res.getBoolean("is_completed"), tasks);
                    break;
                }

                if (!res.isFirst()) {
                    int currentTaskId = res.getInt("task_id");
                    int currentParticipantId = res.getInt("participant_id");

                    if (currentParticipantId > formerParticipantId) {
                        participants.add(new Participant(strings[2],strings[3], strings[4],strings[5],
                                            new Department(departmentNo,strings[0],strings[1])));
                    }
                    if (currentTaskId > formerTaskId) {
                        tasks.add(new Task(workHours, participants, strings[6], strings[7], strings[8], isTaskCompleted));
                    }
                }

                formerTaskId = res.getInt("task_id");
                formerParticipantId = res.getInt("participant_id");
                departmentNo = res.getInt("department_no");
                workHours = res.getDouble("estimated_work_hours");
                isTaskCompleted = res.getBoolean("task_is_completed");

                if (res.isLast()) {
                    if (!(tasks.get(tasks.size()-1).getStart().equals(strings[6]) &&
                            tasks.get(tasks.size()-1).getEnd().equals(strings[7]) &&
                            tasks.get(tasks.size()-1).getTitle().equals(strings[8]))) {
                        tasks.add(new Task(workHours, participants, strings[6], strings[7], strings[8], isTaskCompleted));
                    }
                    assignment = new Assignment(res.getString("assignment_start"), res.getString("assignment_end"),
                            res.getString("assignment_title"),res.getBoolean("is_completed"), tasks);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create assignment from database...\n" + e.getMessage());
            assignment = null;
        }

        return assignment;

    }

    public Task createTask(String assignmentTitle) {

        task = new Task(0,new ArrayList<>());

        projectRepo.putTaskInDatabase(projectRepo.findId("assignment","assignment_title",assignmentTitle,"assignment_id"));

        return task;
    }
    public Task getTask(String taskTitle,String taskStart,String taskEnd) {
        ResultSet res = projectRepo.findTask(taskTitle,taskStart,taskEnd);
        ArrayList<Participant> participants = new ArrayList<>();
        Department department = new Department(0,new String(), new String());

        int formerParticipantId = 0;
        int departmentId = 0;

        String[] strings = new String[16];

        try {
            while (res.next()) {
                // If there is only one row
                if (res.isFirst() && res.isLast()) {
                    task = new Task(res.getDouble("estimated_work_hours"), participants, res.getString("task_start"),
                            res.getString("task_end"), res.getString("task_title"),
                            res.getBoolean("task_is_completed"));
                    break;
                }

                if (!res.isFirst()) {
                    int currentParticipantId = res.getInt("participant_id");

                    if (currentParticipantId > formerParticipantId) {
                        department = new Department(departmentId,strings[0],strings[1]);
                        participants.add(new Participant(strings[2],strings[3],strings[4],strings[5],department));
                    }
                }

                formerParticipantId = res.getInt("participant_id");
                departmentId = res.getInt("department_no");

                if (res.isLast()) {
                    if (!participants.get(participants.size()-1).getId().equals(strings[2])) {
                        department = new Department(departmentId,strings[0],strings[1]);
                        participants.add(new Participant(strings[2],strings[3],strings[4],strings[5],department));
                    }
                    task = new Task(res.getDouble("estimated_work_hours"), participants, strings[6],
                                    strings[7], strings[8], res.getBoolean("task_is_completed"));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't create assignment from database...\n" + e.getMessage());
            task = null;
        }

        return task;
    }

}
