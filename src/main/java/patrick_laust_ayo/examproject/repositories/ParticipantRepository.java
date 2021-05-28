package patrick_laust_ayo.examproject.repositories;

import java.sql.ResultSet;

public class ParticipantRepository extends Repository {
    //TODO en participant har ikke et projectId inde i table i SQL.
    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putParticipantInDatabase(String userId, int departmentNo) {
        executeSQLStatement("INSERT into participant(user_id,participant_name, participant_password, position, department_no) " +
                "VALUES ('" + userId + "', null, null, null " + ", " + departmentNo + ");");
    }

    /*
    public Participant putParticipantInDatabaseWithReturn(Participant participantToInsert, int projectId, Department department) {
        executeSQLStatement("INSERT into participant " +
                "VALUES (default, \" " + participantToInsert.getName() + "\", \"" + participantToInsert.getPosition() +
                "\", " + projectId + ", " + department.getDepartmentNo() + ";");
        ResultSet res = executeQuery("SELECT * FROM project WHERE participant_name = \""
                + participantToInsert.getName() + "\");");


        try {
            participant = new Participant(res.getString("user_id"), res.getString("password"), res.getString("participant_name"),
                    res.getString("position"), department);

        } catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            participant = null;
        }
        closeCurrentConnection();
        return participant;
    }

     */
    //TODO SKAL DENNE METODE RETURNERE USERID?
    public ResultSet findParticipant(String userId) {
        return executeQuery("SELECT * FROM participant " +
                        "INNER JOIN department ON participant.department_no = department.department_no " + "INNER JOIN project " +
                        "WHERE user_id = \"" + userId + "\";");
    }

    public void updateParticipant(String userId, String name, String password, String position, String formerUserId) {
        executeSQLStatement("UPDATE participant " +
                "SET participant.user_id = \"" + userId + "\", " +
                "participant.participant_name = \"" + name + "\", " +
                "participant.participant_password = \"" + password + "\", " +
                "participant.position = \"" + position + "\" " +
                "WHERE participant.user_id = \"" + formerUserId + "\";");
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
