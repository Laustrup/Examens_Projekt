package patrick_laust_ayo.examproject.repositories;

import patrick_laust_ayo.examproject.models.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentRepository extends Repository {

    private Department department;

    public void putDepartmentInDatabase(Department departmentToInsert){
        executeSQLStatement("INSERT INTO department VALUES(\"" +  departmentToInsert.getLocation()
                + "\"," + departmentToInsert.getDepName() + ", default);");
    }

    public Department putDepartmentInDatabaseWithReturn(Department departmentToInsert, int department_No){
        executeSQLStatement("INSERT INTO department VALUES(default, \"" +
                departmentToInsert.getLocation() + "\"," + departmentToInsert.getDepName() + ");");
        ResultSet res = executeQuery("SELECT * FROM department");

        try{
            department = new Department(res.getString("location"),
                    res.getString("department_name"), department_No);
        }
        catch(SQLException e){
            System.out.println("Couldn't create Department " + e.getMessage());
            department = null;
        }

        return department;
    }

}
