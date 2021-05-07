package patrick_laust_ayo.examproject.models;

import patrick_laust_ayo.examproject.repositories.DepartmentRepository;

public class Department {

    private DepartmentRepository repo;

    private String location;
    private String depName;

    private int departmentNo;

    public Department(String location, String depName, int departmentNo) {
        this.location = location;
        this.depName = depName;
        this.departmentNo = departmentNo;
    }
}
