package patrick_laust_ayo.examproject.repositories;

import java.sql.ResultSet;


public class DepartmentRepository extends Repository {

    public ResultSet findDepartment(String depName){
        return executeQuery("SELECT * FROM department WHERE department_name = '" + depName + "';");
    }

    public ResultSet findDepartmentOfEmptyParticipant(int department_no,String projectTitle) {
        return executeQuery("SELECT * FROM department " +
                "INNER JOIN participant ON participant.department_no = department.department_no " +
                "INNER JOIN participant_project ON participant_project.participant_id = participant.participant_id " +
                "INNER JOIN project ON participant_project.project_id = participant_project.project_id " +
                "WHERE participant.user_id = \"Enter user-ID\" AND department.department_no = " + department_no +
                " AND project.title = \"" + projectTitle + "\";");
    }
}
