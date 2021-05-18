package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.services.ExceptionHandler;

import javax.xml.transform.Result;
import java.sql.ResultSet;

public class ParticipantRepository extends Repository {

    private Participant participant;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putParticipantInDatabase(int projectId, int departmentNo) {
        executeSQLStatement("INSERT into participant(user_id,participant_name, participant_password, position,project_id,department_no) " +
                "VALUES (" + null + ", " + null + ", " + null +
                ", " + null +
                ", " + projectId + ", " + departmentNo + ");");
    }

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

    public Participant findParticipant(String userId) {
        ResultSet res = executeQuery("SELECT * FROM participant " +
                        "INNER JOIN department ON participant.department_no = department.department_no " + "INNER JOIN project " +
                        "WHERE user_id = '" + userId + "';");

            updateFoundParticipant(res);
            closeCurrentConnection();

        return participant;

    }

    private void updateFoundParticipant(ResultSet res) {
        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getString("user_id"), res.getString("participant_password"),
                    res.getString("participant_name"), res.getString("position"),
                    department);
        } catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet...\n" + e.getMessage());
            participant = null;
            e.printStackTrace();
        }
    }

    public void updateParticipant(String userId, String name, String password, String position, String formerUserId) {

        executeSQLStatement("UPDATE participant " +
                "SET participant.user_id = '" + userId + "', " +
                "participant.participant_name = '" + name + "', " +
                "participant.participant_password = '" + password + "', " +
                "participant.position = '" + position + "' " +
                "WHERE participant.user_id = '" + formerUserId + "';");
    }

    public void removeParticipant(String userId) {
        executeSQLStatement("DELETE ROW FROM participant WHERE user_id = '" + userId + "';");
    }
}
