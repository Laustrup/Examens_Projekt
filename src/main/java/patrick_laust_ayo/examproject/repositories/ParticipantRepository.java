package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ParticipantRepository extends Repository {

    private Participant participant;

    // puts in database with and without return, for the reason of an option for faster opportunity and testing as well
    public void putParticipantInDatabase(Participant participantToInsert, int projectId, int departmentNo) {
        Connection connection = databaseConnection.getConnection();
        executeSQLStatement(connection,"INSERT into participant \n" +
                "VALUES (default, \" "+ participantToInsert.getName() + "\", \"" +participantToInsert.getPosition() +
                "\", " + projectId + ", " + departmentNo + ";");
    }

    public Participant putParticipantInDatabaseWithReturn(Participant participantToInsert, int projectId, int departmentNo) {
        Connection connection = databaseConnection.getConnection();
        executeSQLStatement(connection,"INSERT into participant \n" +
                "VALUES (default, \" "+ participantToInsert.getName() + "\", \"" + participantToInsert.getPosition() +
                "\", " + projectId + ", " + departmentNo + ";");
        ResultSet res = executeQuery(connection,"SELECT * FROM project WHERE participant_name = \""
                + participantToInsert.getName() + "\";");

        try {
            Department department = new Department()
            participant = new Participant(res.getInt("participant_id"),res.getString("participant_name"),
                    res.getString("position"), department);

        }
        catch (Exception e) {
            System.out.println("Couldn't create a projectmanager from resultSet...\n" + e.getMessage());
            participant = null;
        }

        return participant;
    }

}
