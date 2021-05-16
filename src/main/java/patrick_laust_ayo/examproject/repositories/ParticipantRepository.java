package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.ResultSet;

public class ParticipantRepository extends Repository {

    private Participant participant;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putParticipantInDatabase(int projectId, int departmentNo) {
        executeSQLStatement("INSERT into participant " +
                "VALUES (default, " + null + ", " + null +
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
            participant = new Participant(res.getInt("participant_id"), res.getString("password"), res.getString("participant_name"),
                    res.getString("position"), department);

        } catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            participant = null;
        }

        return participant;
    }

    //TODO create a new Project constructor if less parameters is needed
    public Participant findParticipant(String name) {

        ResultSet res = executeQuery("SELECT * FROM participant " +
                "INNER JOIN department ON participant.department_no = department.department_no" + " INNER JOIN project " +
                "WHERE participant_name = '" + name + "';");

        try {
            res.next();

            Department department = new Department(res.getInt("department_no"),
                    res.getString("location"), res.getString("department_name"));
            participant = new Participant(res.getInt("participant_id"), res.getString("participant_password"),
                    res.getString("participant_name"),
                     res.getString("position"),
                    department);
        } catch (Exception e) {
            System.out.println("Couldn't create a participant from resultSet...\n" + e.getMessage());
            participant = null;
        }

        return participant;
    }

    public void updateParticipant(Participant participant,String formerName) {

        executeSQLStatement("UPDATE participant " +
                "SET participant_name = '" + participant.getName() + "', " +
                "participant_password = '" + participant.getPassword() + "', " +
                "position = '" + participant.getPosition() +
                "' WHERE participant_name = '" + formerName + "';");
    }
}
