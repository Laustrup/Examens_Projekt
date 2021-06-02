package patrick_laust_ayo.examproject.models;

//Authors Ayo,Patrick and Laust
public class Department {

    private String location;
    private String depName;

    private int departmentNo;

    public Department(int departmentNo, String location, String depName) {
        this.location = location;
        this.depName = depName;
        this.departmentNo = departmentNo;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public int getDepartmentNo() {
        return departmentNo;
    }

    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }
}
