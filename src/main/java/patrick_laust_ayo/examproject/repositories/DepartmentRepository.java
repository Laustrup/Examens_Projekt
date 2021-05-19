package patrick_laust_ayo.examproject.repositories;

import java.sql.ResultSet;

public class DepartmentRepository extends Repository {

    /*
    public void putDepartmentInDatabase(Department departmentToInsert){
        executeSQLStatement("INSERT INTO department VALUES(\"" + departmentToInsert.getDepartmentNo() +
                "\", \"" + departmentToInsert.getLocation()
                + "\"," + departmentToInsert.getDepName() + ");");
    }

     */

    /*
    public Department putDepartmentInDatabaseWithReturn(Department departmentToInsert){
        executeSQLStatement("INSERT INTO department VALUES(\"" + departmentToInsert.getDepartmentNo() + "\", \"" +
                departmentToInsert.getLocation() + "\"," + departmentToInsert.getDepName() + ");");
        ResultSet res = executeQuery("SELECT * FROM department");
        closeCurrentConnection();

        try{
            department = new Department(res.getInt("department_no"),
                    res.getString("location") ,res.getString("department_name"));
        }
        catch(SQLException e){
            System.out.println("Couldn't create Department " + e.getMessage());
            department = null;
        }

        return department;
    }

     */

    public ResultSet findDepartment(String depName){
        return executeQuery("SELECT * FROM department WHERE department_name = '" + depName + "';");
    }
}
