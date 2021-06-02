package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;


public class ProjectRepository extends Repository{


    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project(title, projectmanager_id) VALUES (\""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
    }

    public void putPhaseInDatabase(String phaseTitle, int projectId, String projectManagerUsername) {
        int assignmentId = calcNextId("assignment");
        int taskId = calcNextId("task");
        int participantId = findId("participant","user_id",projectManagerUsername,"participant_id");
        executeSQLStatement("INSERT INTO phase_table(phase_title, project_id) VALUES (\"" + phaseTitle + "\", " + projectId + ");");
        executeSQLStatement("INSERT INTO assignment(assignment_title, assignment_start, assignment_end, is_Completed, phase_id) " +
                "VALUES (null, null, null, false, " + findId("phase_table", "phase_title",phaseTitle,"phase_id") + "); ");
        executeSQLStatement("INSERT INTO task(assignment_id,estimated_work_hours,task_title,task_start,task_end,task_is_completed) " +
                "VALUES (" + assignmentId + ", null, null,null,null,false); ");
        executeSQLStatement("INSERT INTO participant_task(participant_id,task_id) " +
                "VALUES (" + participantId + "," + taskId + "); ");

    }

    public ResultSet findPhase(String phaseTitle,String projectTitle) {
        ResultSet res = executeQuery("SELECT * FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "WHERE phase_title = \"" + phaseTitle + "\" AND project.title = \"" + projectTitle + "\";");
        try {
            res.next();
            res.getString("task_title");
            return executeQuery("SELECT * FROM phase_table " +
                    "INNER JOIN project ON project.project_id = phase_table.project_id " +
                    "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                    "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                    "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                    "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                    "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";");
        }
        catch (Exception e) {
            System.out.println("No task title in findPhase...\n");
        }
        res = executeQuery("SELECT * FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";");
        try {
            res.next();
            res.getString("assignment_title");
            return executeQuery("SELECT * FROM phase_table " +
                    "INNER JOIN project ON project.project_id = phase_table.project_id " +
                    "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                    "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                    "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";");
        }
        catch (Exception e) {
            System.out.println("No assignment in findPhase...\n");
        }
        return executeQuery("SELECT * FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";");
    }

    public void updatePhase(String phaseTitle,String projectTitle,String formerPhaseTitle) {
        if (phaseTitle == null) {
            executeSQLStatement("UPDATE phase_table " +
                    "INNER JOIN project ON project.project_id = phase_table.project_id " +
                    "SET phase_table.phase_title = \"" + phaseTitle + "\" " +
                    "WHERE phase_table.phase_title = null AND project.title = \"" + projectTitle + "\";");
        }
        else {
        executeSQLStatement("UPDATE phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "SET phase_table.phase_title = \"" + phaseTitle + "\" " +
                "WHERE phase_table.phase_title = \"" + formerPhaseTitle + "\" AND project.title = \"" + projectTitle + "\";");
        }
    }

    public void removePhase(String phaseTitle,String projectTitle) {
        executeSQLStatement("DELETE ROW FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id" +
                "WHERE phase_title = \"" + phaseTitle + "\" AND project.title = \"" + projectTitle + "\";");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId) {
        executeSQLStatement("INSERT INTO assignment(assignment_title, assignment_start, assignment_end, is_Completed, phase_id) " +
                            "VALUES (\"" + assignment.getTitle() + "\", \""  + assignment.getStart() +  "\", \"" + assignment.getEnd() + "\", " +
                            assignment.isCompleted() + ", " + phaseId + "); ");
    }

    public void updateAssignment(String title,String start,String end, String formerTitle,String phaseTitle) {
        executeSQLStatement("UPDATE assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "SET assignment.assignment_title " + " = \"" + title + "\", " +
                "assignment.assignment_start " + " = \"" + start + "\", " +
                "assignment.assignment_end " + " = \"" + end + "\" " +
                "WHERE assignment.assignment_title = \"" + formerTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
    }

    public void updateAssignmentIsCompleted(String isCompleted, String assignmentTitle, String phaseTitle) {
        executeSQLStatement("UPDATE assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "SET assignment.assignment_is_completed " + " = " + isCompleted + ", " +
                "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
    }

    public ResultSet findAssignment(String assignmentTitle,String phaseTitle) {
        ResultSet res = executeQuery("SELECT * FROM assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
        try {
            res.next();
            res.getString("participant_name");
            return executeQuery("SELECT * FROM assignment " +
                    "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                    "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                    "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                    "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
        }
        catch (Exception e) {
            System.out.println("No participant name in findAssignment...\n");
        }
        res = executeQuery("SELECT * FROM assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
        try {
            res.next();
            res.getString("task_title");
            return executeQuery("SELECT * FROM assignment " +
                    "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                    "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                    "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
        }
        catch (Exception e) {
            System.out.println("No task title in findAssignment...\n");
        }
        return executeQuery("SELECT * FROM assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.phase_title = \"" + phaseTitle + "\";");
    }

    public void removeAssignment(String assignmentTitle,String taskTitle) {
        executeSQLStatement("DELETE ROW FROM assignment " +
                "INNER JOIN phase ON phase.phase_id = assignment.phase_id" +
                "WHERE task_title = \"" + assignmentTitle + "\" AND assignment_title = \"" + taskTitle + "\";");
    }

    public void putTaskInDatabase(int assignmentId) {
        executeSQLStatement("INSERT INTO task(assignment_id,estimated_work_hours) VALUES (" + assignmentId + ", null); ");
    }

    public boolean addParticipantToTask(String userId, Task task) {
        int participantId = findId("participant","user_id",userId,"participant_id");
        ResultSet res = findTask(task.getTitle(), task.getStart(), task.getEnd());
        try{
            res.next();
            executeSQLStatement("INSERT INTO participant_task(participant_id,task_id) " +
                            "VALUES (" + participantId + ", " + res.getInt("task_id") + ");");
            return true;
        }
        catch (Exception e) {
            System.out.println("Couldn't insert participant into participant_task...\n" + e.getMessage());
            return false;
        }
    }

    public ResultSet findTask(String taskTitle,String taskStart,String taskEnd) {
        ResultSet res = executeQuery("SELECT * FROM task " +
                "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE task.task_title = \"" + taskTitle + "\" AND task.task_start = \"" + taskStart + "\" " +
                "AND task.task_end = \"" + taskEnd + "\";");
        try {
            res.next();
            res.getString("participant_title");
            return executeQuery("SELECT * FROM task " +
                    "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                    "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                    "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "WHERE task.task_title = \"" + taskTitle + "\" AND task.task_start = \"" + taskStart + "\" " +
                    "AND task.task_end = \"" + taskEnd + "\";");
        }
        catch (Exception e) {
            System.out.println("No participant name in findTask...\n");
        }
        return executeQuery("SELECT * FROM task " +
                "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                "WHERE task.task_title = \"" + taskTitle + "\" AND task.task_start = \"" + taskStart + "\" " +
                "AND task.task_end = \"" + taskEnd + "\";");
    }

    public void updateTask(String title,String start,String end, String workHours, String formerTitle,String assignmentTitle) {
        executeSQLStatement("UPDATE task " +
                "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                "SET task.task_title " + " = \"" + title + "\", " +
                "task.task_start " + " = \"" + start + "\", " +
                "task.task_end " + " = \"" + end + "\", " +
                "task.estimated_work_hours = \"" + workHours + "\" " +
                "WHERE task.task_title = \"" + formerTitle + "\" AND assignment.assignment_title = \"" + assignmentTitle + "\";");
    }

    public void updateTaskIsCompleted(String isCompleted, Task task) {
        executeSQLStatement("UPDATE task " +
                "SET task.task_is_completed = " + isCompleted + ", " +
                "WHERE task.task_title = \"" + task.getTitle() + "\" " +
                "AND task.task_start = \"" + task.getStart() + "\" " +
                "AND task.task_end = \"" + task.getEnd() + "\";");
    }

    public void removeTask(String taskTitle,String assignmentTitle) {
        executeSQLStatement("DELETE ROW FROM project " +
                            "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                            "INNER JOIN assignment ON assignment.assignment_id = phase_table.phase_id " +
                            "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                            "WHERE task_title = \"" + taskTitle + "\" AND assignment_title = \"" + assignmentTitle + "\";");
    }

    public ResultSet findProject(String projectTitle) {
        ResultSet res = executeQuery("SELECT * FROM project " +
                "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "INNER JOIN participant_project ON participant_project.participant_id = participant.participant_id " +
                "WHERE project.title = \"" + projectTitle +  "\" " +
                "AND participant.department_no = department.department_no " +
                "AND participant.participant_id = participant_project.participant_id " +
                "AND project.project_id = participant_project.project_id;");
        try {
            res.next();
            res.getString("task_title");
            return executeQuery("SELECT * FROM project " +
                    "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                    "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                    "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                    "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                    "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                    "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "INNER JOIN participant_project ON participant_project.participant_id = participant.participant_id " +
                    "WHERE project.title = \"" + projectTitle +  "\" " +
                    "AND participant.department_no = department.department_no " +
                    "AND participant.participant_id = participant_project.participant_id " +
                    "AND project.project_id = participant_project.project_id;");
        }
        catch (Exception e) {
            System.out.println("No task title in findProject...\n");
        }
        // Without task
        res = executeQuery("SELECT * FROM project " +
                "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN participant_project " +
                "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE project.title = \"" + projectTitle +  "\" " +
                "AND participant.department_no = department.department_no " +
                "AND participant.participant_id = participant_project.participant_id " +
                "AND project.project_id = participant_project.project_id;");
        try {
            res.next();
            res.getString("assignment_title");
            return executeQuery("SELECT * FROM project " +
                    "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                    "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                    "INNER JOIN participant_project " +
                    "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                    "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "WHERE project.title = \"" + projectTitle +  "\" " +
                    "AND participant.department_no = department.department_no " +
                    "AND participant.participant_id = participant_project.participant_id " +
                    "AND project.project_id = participant_project.project_id;");
        }
        catch (Exception e) {
            System.out.println("No assignment title in findProject...\n");
        }
        // Without assignment
        res = executeQuery("SELECT * FROM project " +
                "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                "INNER JOIN participant_project " +
                "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE project.title = \"" + projectTitle +  "\" " +
                "AND participant.department_no = department.department_no " +
                "AND participant.participant_id = participant_project.participant_id " +
                "AND project.project_id = participant_project.project_id;");
        try {
            res.next();
            res.getString("phase_table.phase_title");
            return executeQuery("SELECT * FROM project " +
                    "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                    "INNER JOIN participant_project " +
                    "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                    "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                    "INNER JOIN department ON department.department_no = participant.department_no " +
                    "WHERE project.title = \"" + projectTitle +  "\" " +
                    "AND participant.department_no = department.department_no " +
                    "AND participant.participant_id = participant_project.participant_id " +
                    "AND project.project_id = participant_project.project_id;");
        }
        catch (Exception e) {
            System.out.println("No phase title in findProject...\n");
        }
        // Without phase
        return executeQuery("SELECT * FROM project " +
                "INNER JOIN participant_project " +
                "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE project.title = \"" + projectTitle +  "\" " +
                "AND participant.department_no = department.department_no " +
                "AND participant.participant_id = participant_project.participant_id " +
                "AND project.project_id = participant_project.project_id;");
    }

    public ResultSet findProjects(String userId) {
        return executeQuery("SELECT * FROM project " +
                "INNER JOIN participant_project ON participant_project.project_id = project.project_id " +
                "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                "WHERE participant.user_id = \"" + userId + "\";");
    }

    public void updateProject(String currentTitle,String formerTitle) {
        executeSQLStatement("UPDATE project " +
                "SET title = \"" + currentTitle + "\" " +
                "WHERE title = \"" + formerTitle + "\";");
    }

    public void addParticipantToProject(Participant participant, Project project) {
        ResultSet res = executeQuery("SELECT * FROM participant " +
                "INNER JOIN participant_project ON participant_project.participant_id = participant.participant_id " +
                "INNER JOIN project ON project.project_id = participant_project.project_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE user_id = \"Enter user-ID\" AND project.title = \"" + project.getTitle() + "\" AND department.department_no = "
                + participant.getDepartment().getDepartmentNo() + ";");

        int emptyParticipantId = -1;

        try {
            res.next();
            emptyParticipantId = res.getInt("participant_id");
        }
        catch (Exception e) {
            System.err.println("Couldn't get emptyParticipantId...\n");
        }

        int participantId = findId("participant","user_id", participant.getId(),"participant_id");
        int projectId = findId("project","title", project.getTitle(),"project_id");


        executeSQLStatement("UPDATE participant_project " +
                "INNER JOIN project ON participant_project.project_id = project.project_id " +
                "INNER JOIN participant ON participant_project.participant_id = participant.participant_id " +
                "SET participant_project.participant_id = " + participantId + " " +
                "WHERE participant_project.participant_id = " + emptyParticipantId + " AND participant_project.project_id = " + projectId + ";");
        executeSQLStatement("DELETE participant FROM participant " +
                "WHERE participant.user_id = \"Enter user-ID\" " +
                "AND participant_id = " + emptyParticipantId + " ;");
    }

    public void removeProject(String projectTitle) {
        executeSQLStatement("DELETE participant_task " +
                "FROM participant_task " +
                "INNER JOIN task ON task.task_id = participant_task.task_id " +
                "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "INNER JOIN project ON phase_table.project_id = project.project_id " +
                "WHERE project.title = \"" + projectTitle + "\";");


        executeSQLStatement("DELETE task " +
                "FROM task " +
                "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "INNER JOIN project ON phase_table.project_id = project.project_id " +
                "WHERE project.title = \"" + projectTitle + "\";");


        executeSQLStatement("DELETE assignment " +
                "FROM assignment " +
                "INNER JOIN phase_table ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN project ON phase_table.project_id = project.project_id " +
                "WHERE project.title = \"" + projectTitle + "\";");


        executeSQLStatement("DELETE phase_table " +
                "FROM phase_table " +
                "INNER JOIN project ON phase_table.project_id = project.project_id " +
                "WHERE project.title = \"" + projectTitle + "\";");


        executeSQLStatement("DELETE participant_project " +
                "FROM participant_project " +
                "INNER JOIN project ON participant_project.project_id = project.project_id " +
                "WHERE project.title = \"" + projectTitle + "\";");


        executeSQLStatement("DELETE FROM project " +
                "WHERE title = \"" + projectTitle + "\";");
    }
}
