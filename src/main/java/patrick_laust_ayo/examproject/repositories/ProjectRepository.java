package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;

public class ProjectRepository extends Repository{

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putProjectInDatabase(Project projectToInsert, int projectmanagerId) {
        executeSQLStatement("INSERT INTO project(title, projectmanager_id) VALUES (\""  + projectToInsert.getTitle() + "\", " + projectmanagerId + "); ");
    }

    /*
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
        closeCurrentConnection();
        return project;
    }

     */

    public void putPhaseInDatabase(int projectmanagerId) {
        executeSQLStatement("INSERT INTO phase VALUES (default, "  + null + ", " + projectmanagerId + "); ");
    }
    public ResultSet findPhase(String phaseTitle, String projectTitle) {
        return executeQuery("SELECT * FROM phase_table " +
                "INNER JOIN project ON project.project_id = phase_table.project_id " +
                "INNER JOIN assignment ON assignment.phase_id = phase_table.phase_id " +
                "INNER JOIN task ON task.assignment_id = assignment.assignment_id " +
                "INNER JOIN participant_task ON participant_task.task_id = task.task_id " +
                "INNER JOIN participant ON participant.participant_id = participant_task.participant_id " +
                "INNER JOIN department ON department.department_no = participant.department_no " +
                "WHERE phase_title = " + phaseTitle + " AND title = " + projectTitle + ";");
    }

    public void putAssignmentInDatabase(Assignment assignment, int phaseId) {
        executeSQLStatement("INSERT INTO assignment VALUES (default, "  + assignment.getStart() +
                            ", " + assignment.getEnd() + ", " + assignment.isCompleted() + ", " +
                             phaseId + "); ");
    }

    public void putTaskInDatabase(int assignmentId) {
        executeSQLStatement("INSERT INTO task VALUES (" + assignmentId + ", " + null + "); ");
    }

    public ResultSet findProject(String projectTitle) {

        return executeQuery("SELECT project.project_id, project.title, project.projectmanager_id, " +
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
                "WHERE project.title = '" + projectTitle +  "'" +
                "and participant.department_no = department.department_no;");

    }

    // TODO Not done, when used logic should be moved to service
    public ResultSet findProjects(String userId) {
        return executeQuery("SELECT * FROM project " +
                                        "INNER JOIN participant_project ON participant_project.project_id = project.project_id " +
                                        "INNER JOIN participant ON participant.participant_id = participant_project.participant_id " +
                                        "WHERE participant.user_id = '" + userId + "';");
    }

    public void updateProject(Project project,String formerTitle) {
        executeSQLStatement("UPDATE project " +
                "SET title = '" + project.getTitle() + "', " +
                "WHERE projectmanager_username = '" + formerTitle + "';");
    }

    // TODO Figure the sql statement to fit the project and participant
    public void addParticipantToProject(Participant participant, Project project) {
        // TODO Should projecttitle be uniq? To insure not to be added to wrong project?
        executeSQLStatement("UPDATE participant_project " +
                "INNER JOIN project ON participant_project.project_id = project.project_id " +
                "INNER JOIN participant ON participant_project.participant_id = participant.participant_id " +
                "ADD participant_id = " + findId("participant","user_id", participant.getId(), "participant_id") + ", " +
                "ADD project_id = " + findId("project","title", project.getTitle(), "project_id") + ", " +
                "WHERE participant.user_id = '" + participant.getId() + "'; " +
                "DELETE ROW FROM participant WHERE project.user_id = " + null + ", WHERE participant_project.project_id = participant_project.participant_id;");
    }

    public void removeProject(String title) {
        executeSQLStatement("DELETE ROW FROM project WHERE title = '" + title + "';");
    }

}
