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
            department = new Department(res.getInt("department_no"),
                    res.getString("location") ,res.getString("department_name"));
        }
        catch(SQLException e){
            System.out.println("Couldn't create Department " + e.getMessage());
            department = null;
        }

        return department;
    }

    public Department findDepartment(String depName){
        ResultSet res = executeQuery("SELECT * FROM department WHERE department_name = '" + depName + "';");
        Department departmentByName = null;

        try{
            res.next();
            departmentByName = new Department(res.getInt(1), res.getString(2), res.getString(3));
        }
        catch(Exception e){
            System.out.println("Couldn't find department by name " + e.getMessage());
        }
        return departmentByName;
    }
}
