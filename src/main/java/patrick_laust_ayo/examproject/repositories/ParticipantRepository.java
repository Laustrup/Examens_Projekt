package patrick_laust_ayo.examproject.repositories;

import java.sql.ResultSet;


public class ParticipantRepository extends Repository {


    public void putParticipantsInDatabase(String userId, int departmentNo, int amount) {
        for (int i = 0; i < amount; i++) {
            executeSQLStatement(" INSERT INTO participant(user_id,participant_name, participant_password, position, department_no) " +
                    "VALUES (\"" + userId + "\", null, null, null, " + departmentNo + "); ");
        }
    }

    public void putParticipantsInDatabase(String userId, String name, String password, String position, int departmentNo, int amount) {
        for (int i = 0; i < amount; i++) {
            executeSQLStatement(" INSERT INTO participant(user_id,participant_name, participant_password, position, department_no) " +
                    "VALUES (\"" + userId + "\", \"" + name + "\", \"" + password + "\", \"" + position + "\", " + departmentNo + "); ");
        }
    }

    public void putParticipantsInParticipantProjectTable(String userId, String projectTitle, int amount){
        for (int i = 0; i < amount;i++) {
            executeSQLStatement(" INSERT INTO participant_project(participant_id, project_id) " +
                    "VALUES (" + findId("participant", "user_id", userId, "participant_id") +
                    ", " + findId("project", "title", projectTitle, "project_id") +
                    "); ");
        }
    }

    public ResultSet findParticipant(String userId) {
        return executeQuery("SELECT * FROM participant " +
                        "INNER JOIN department ON participant.department_no = department.department_no " + "INNER JOIN project " +
                        "WHERE user_id = \"" + userId + "\";");
    }

    public void updateParticipant(String userId, String name, String password, String position, String departmentName, String formerUserId) {
        int departmentId = findId("department","department_name",departmentName,"department_no");

        if (name.equals("null") && position.equals("null")){
            executeSQLStatement("UPDATE participant " +
                    "SET participant.user_id = \"" + userId + "\", " +
                    "participant.participant_name = null, " +
                    "participant.participant_password = \"" + password + "\", " +
                    "participant.position = null " +
                    "participant.department_no = " + departmentId + " " +
                    "WHERE participant.user_id = \"" + formerUserId + "\";");
        }
        else {
            executeSQLStatement("UPDATE participant " +
                    "SET participant.user_id = \"" + userId + "\", " +
                    "participant.participant_name = \"" + name + "\", " +
                    "participant.participant_password = \"" + password + "\", " +
                    "participant.position = \"" + position + "\" " +
                    "participant.department_no = " + departmentId + " " +
                    "WHERE participant.user_id = \"" + formerUserId + "\";");
        }
    }

    public void addParticipantToTask(int participantId, int taskId) {
        executeSQLStatement("INSERT INTO participant_task(participant_id,project_id) " +
                "VALUES (" + participantId + ", " + taskId + ");");
    }

    public void removeParticipantFromTask(int participantId, int taskId) {
        executeSQLStatement("DELETE ROW FROM participant_task WHERE participant_id = " + participantId +
                " AND task_id = " + taskId + ";" );
    }

    public void removeParticipant(String userId) {
        try {
            executeSQLStatement("DELETE ROW FROM participant WHERE user_id = \"" + userId + "\";");
        }
        catch (Exception e) {
            System.out.println("Couldn't remove participant...\n" + e.getMessage());
        }
    }
}
