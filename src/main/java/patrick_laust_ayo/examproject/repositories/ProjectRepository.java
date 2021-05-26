package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;

public class ProjectRepository extends Repository{

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project(title, projectmanager_id) VALUES (\""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
    }

    public void putPhaseInDatabase(int projectId) {
        executeSQLStatement("INSERT INTO phase_table(phase_title, project_id) VALUES (null, " + projectId + ");");
    }
    public ResultSet findPhase(String phaseTitle,String projectTitle) {
        return executeQuery("SELECT * FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "WHERE phase_title = \"" + phaseTitle + "\" AND title = \"" + projectTitle + "\";") ;
                //TODO denne ORDER BY sorterer participant id'erne numerisk.
                //+ "ORDER BY participant.participant_id;");
    }
    // TODO Change safeupdate
    public void updatePhase(String phaseTitle,String projectTitle,String formerPhaseTitle) {
        executeSQLStatement("UPDATE phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "SET phase_table.phase_title = \"" + phaseTitle + "\" " +
                "WHERE phase_table.phase_title = \"" + formerPhaseTitle + "\" AND project.title = \"" + projectTitle + "\";");
    }
    public void removePhase(String assignmentTitle,String projectTitle) {
        executeSQLStatement("DELETE ROW FROM phase " +
                "INNER JOIN project ON project.project_id = phase.project_id" +
                "WHERE task_title = \"" + assignmentTitle + "\" AND title = \"" + projectTitle + "\";");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId) {
        executeSQLStatement("INSERT INTO assignment(assignment_title, assignment_start, assignment_end, is_Completed, phase_id) " +
                            "VALUES (null, \""  + assignment.getStart() +  "\", \"" + assignment.getEnd() + "\", " +
                            assignment.isCompleted() + ", " + phaseId + "); ");
    }
    // TODO Change safeupdate
    public void updateAssignment(String title,String start,String end,String isCompleted, String formerTitle,String phaseTitle) {
        executeSQLStatement("UPDATE assignment " +
                "INNER JOIN phase ON phase.phase_id = assignment.phase_id " +
                "SET assignment.assignment_title " + " = \"" + title + "\", " +
                "SET assignment.assignment_start " + " = \"" + start + "\", " +
                "SET assignment.assignment_end " + " = \"" + end + "\", " +
                "SET assignment.assignment_is_completed " + " = " + isCompleted + ", " +
                "WHERE assignment.assignment_title = \"" + formerTitle + "\" AND phase.title " + phaseTitle + ";");
    }
    public ResultSet findAssignment(String assignmentTitle,String phaseTitle) {
        return executeQuery("SELECT * FROM assignment " +
                "INNER JOIN phase_table ON phase_table.phase_id = assignment.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE assigment_title = \"" + assignmentTitle + "\" AND phase_title = \"" + phaseTitle + "\";");
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
        return executeQuery("SELECT * FROM task " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE task_title = \"" + taskTitle + "\" AND task_start = \"" + taskStart + "\" " +
                "AND task_end = \"" + taskEnd + "\";");
    }
    public void removeTask(String taskTitle,String assignmentTitle) {
        executeSQLStatement("DELETE ROW FROM project " +
                            "INNER JOIN assignment ON assignment.assignment_id = task.assignment_id" +
                            "WHERE task_title = \"" + taskTitle + "\" AND assignment_title = \"" + assignmentTitle + "\";");
    }

    public ResultSet findProject(String projectTitle) {
        return executeQuery("SELECT * FROM project " +
                "INNER JOIN phase_table ON phase_table.project_id = project.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN projectmanager ON projectmanager.projectmanager_id = project.projectmanager_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "INNER JOIN participant_project " +
                "WHERE project.title = \"" + projectTitle +  "\" " +
                "AND participant.department_no = department.department_no " +
                "AND participant.participant_id = participant_project.participant_id " +
                "AND project.project_id = participant_project.project_id;");

    }
    // TODO Not done, when used logic should be moved to service
    public ResultSet findProjects(String userId) {
        return executeQuery("SELECT * FROM project " +
                                        "INNER JOIN participant_project ON participant_project.project_id = project.project_id " +
                                        "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                                        "WHERE participant.user_id = \"" + userId + "\";");
    }
    public void updateProject(String currentTitle,String formerTitle) {
        executeSQLStatement("UPDATE project " +
                "SET title = \"" + currentTitle + "\", " +
                "WHERE projectmanager_username = \"" + formerTitle + "\";");
    }
    // TODO Figure the sql statement to fit the project and participant
    public void addParticipantToProject(Participant participant, Project project) {
        // TODO Should projecttitle be uniq? To insure not to be added to wrong project?
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
    public void removeProject(String title) {
        executeSQLStatement("DELETE ROW FROM project WHERE title = \"" + title + "\";");
    }

}
