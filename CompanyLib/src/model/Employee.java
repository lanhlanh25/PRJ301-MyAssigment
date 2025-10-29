package model;

import model.iam.User;

public class Employee extends BaseModel {
    private String name;
    private Division dept; 
    private Employee supervisor;
    private User user;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Division getDept() {
        return dept;
    }

    public void setDept(Division dept) {
        this.dept = dept;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}