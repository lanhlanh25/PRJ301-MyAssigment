package model.iam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.BaseModel;
import model.Employee;

public class User extends BaseModel {

    private String username;
    private String password;
    private String displayname;
    private Employee employee;
    private ArrayList<Role> roles = new ArrayList<>();

    // BỔ SUNG: Map lưu trữ các URL mà User có quyền truy cập
    private Map<String, Boolean> authorizedFeatures = new HashMap<>();

    // ... (Các Getter/Setter khác) ...
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
// ... (và các phương thức set khác: setId, setEmployee, v.v.)

    // Getter/Setter cho Authorized Features
    public String getDisplayname() {
        return displayname;
    }

    public Map<String, Boolean> getAuthorizedFeatures() {
        return authorizedFeatures;
    }

    public void setAuthorizedFeatures(Map<String, Boolean> authorizedFeatures) {
        this.authorizedFeatures = authorizedFeatures;
    }
}
