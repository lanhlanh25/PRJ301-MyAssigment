package model;

/**
 *
 * @author sonnt
 */
public class Employee extends BaseModel {
    private String name;
    // THAY THẾ Department bằng Division
    private Division dept; 
    private Employee supervisor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // THAY THẾ Department bằng Division
    public Division getDept() {
        return dept;
    }

    // THAY THẾ Department bằng Division
    public void setDept(Division dept) {
        this.dept = dept;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }
    
}