package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentRepository extends Repository {

    private Department department;

    public void putDepartmentInDatabase(Department departmentToInsert){
        executeSQLStatement("INSERT INTO department VALUES(\"" + departmentToInsert.getDepartmentNo() +
                "\", \"" + departmentToInsert.getLocation()
                + "\"," + departmentToInsert.getDepName() + ");");
    }

    public Department putDepartmentInDatabaseWithReturn(Department departmentToInsert){
        executeSQLStatement("INSERT INTO department VALUES(\"" + departmentToInsert.getDepartmentNo() + "\", \"" +
                departmentToInsert.getLocation() + "\"," + departmentToInsert.getDepName() + ");");
        ResultSet res = executeQuery("SELECT * FROM department");

        try{
            department = new Department(res.getString("location"),
                    res.getString("department_name"), res.getInt("department_no"));
        }
        catch(SQLException e){
            System.out.println("Couldn't create Department " + e.getMessage());
            department = null;
        }

        return department;
    }

}
