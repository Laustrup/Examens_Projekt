package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;

public class ProjectRepository extends Repository{

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project(title, projectmanager_id) VALUES (\""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
    }


    public void putPhaseInDatabase(String projectTitle, String number, int projectId) {
        executeSQLStatement("INSERT INTO phase_table(phase_title, project_id) VALUES (\"" + projectTitle
                + "\" " + "\" - NEW PHASE \"" + " \"" + number + "\", " + projectId + ");");
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
                "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";");
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
        //TODO project creator har muligvis problemer med getPhase metoden, fordi den ikke kan finde
        //TODO Department No ud fra dette resultset når man lige har lavet en fase, og laver en til.
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
    public void removePhase(String assignmentTitle,String projectTitle) {
        executeSQLStatement("DELETE ROW FROM phase " +
                "INNER JOIN project ON project.project_id = phase.project_id" +
                "WHERE task_title = \"" + assignmentTitle + "\" AND title = \"" + projectTitle + "\";");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId) {
        executeSQLStatement("INSERT INTO assignment(assignment_title, assignment_start, assignment_end, is_Completed, phase_id) " +
                            "VALUES (\"" + assignment.getTitle() + "\", \""  + assignment.getStart() +  "\", \"" + assignment.getEnd() + "\", " +
                            assignment.isCompleted() + ", " + phaseId + "); ");
    }
    // TODO Change safeupdate
    public void updateAssignment(String title,String start,String end, String formerTitle,String phaseTitle) {
        executeSQLStatement("UPDATE assignment " +
                "INNER JOIN phase ON phase.phase_id = assignment.phase_id " +
                "SET assignment.assignment_title " + " = \"" + title + "\", " +
                "SET assignment.assignment_start " + " = \"" + start + "\", " +
                "SET assignment.assignment_end " + " = \"" + end + "\", " +
                "WHERE assignment.assignment_title = \"" + formerTitle + "\" AND phase.title \"" + phaseTitle + "\";");
    }
    public void updateAssignmentIsCompleted(String isCompleted, String assignmentTitle, String phaseTitle) {
        executeSQLStatement("UPDATE assignment " +
                "INNER JOIN phase ON phase.phase_id = assignment.phase_id " +
                "SET assignment.assignment_is_completed " + " = " + isCompleted + ", " +
                "WHERE assignment.assignment_title = \"" + assignmentTitle + "\" AND phase_table.title \"" + phaseTitle + "\";");
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
                "SET task.task_start " + " = \"" + start + "\", " +
                "SET task.task_end " + " = \"" + end + "\", " +
                "SET task.estimated_work_hours = " + workHours + " " +
                "WHERE task.task_title = \"" + formerTitle + "\" AND assignment.title \"" + assignmentTitle + "\";");
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
                            "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id" +
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
        int participantId = findId("participant","user_id", participant.getId(),"participant_id");
        int projectId = findId("project","title", project.getTitle(),"project_id");

        executeSQLStatement("UPDATE participant_project " +
                "INNER JOIN project ON participant_project.project_id = project.project_id " +
                "INNER JOIN participant ON participant_project.participant_id = participant.participant_id " +
                "SET participant_project.participant_id = " + participantId + " " +
                "WHERE participant_project.participant_id = " + participantId + " AND participant_project.project_id = " + projectId + ";" +
                "DELETE ROW FROM participant " +
                "INNER JOIN participant_project ON participant_project.participant_id = participant.participant_id " +
                "INNER JOIN project ON project.project_id = participant_project.project_id " +
                "WHERE project.user_id = null " +
                "AND WHERE project.name = null " +
                "AND WHERE project.password = null " +
                "AND WHERE position = null " +
                "AND WHERE participant_project.project_id = participant_project.participant_id" +
                "AND WHERE participant.participant_id = participant_project.participant_id;");
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
