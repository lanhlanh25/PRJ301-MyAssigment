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

    // Map lưu trữ các URL mà User có quyền truy cập
    private Map<String, Boolean> authorizedFeatures = new HashMap<>();

    // ====== Constructors ======
    public User() {
    }

    // ====== Getter & Setter ======
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

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

    public Map<String, Boolean> getAuthorizedFeatures() {
        return authorizedFeatures;
    }

    public void setAuthorizedFeatures(Map<String, Boolean> authorizedFeatures) {
        this.authorizedFeatures = authorizedFeatures;
    }

    // ====== Phương thức hỗ trợ ======
    /** 
     * Thêm một Role vào danh sách nếu chưa tồn tại 
     */
    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    /** 
     * Kiểm tra quyền truy cập (URL)
     */
    public boolean hasAccess(String url) {
        return authorizedFeatures != null && authorizedFeatures.getOrDefault(url, false);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", displayname='" + displayname + '\'' +
                ", employee=" + (employee != null ? employee.getName() : "null") +
                ", roles=" + roles +
                '}';
    }
}
