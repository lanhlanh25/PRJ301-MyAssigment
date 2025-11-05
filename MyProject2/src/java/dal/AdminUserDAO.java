package dal;

import java.sql.*;
import model.AdminUser;
import java.util.ArrayList;

public class AdminUserDBContext extends DBContext {

    public AdminUser get(String username, String password) {
        try {
            String sql = "SELECT * FROM AdminUser WHERE username=? AND password=?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                AdminUser a = new AdminUser();
                a.setAdminId(rs.getInt("admin_id"));
                a.setUsername(rs.getString("username"));
                a.setFullname(rs.getString("fullname"));
                return a;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<AdminUser> list() {
        ArrayList<AdminUser> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM AdminUser";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                AdminUser a = new AdminUser();
                a.setAdminId(rs.getInt("admin_id"));
                a.setUsername(rs.getString("username"));
                a.setFullname(rs.getString("fullname"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(AdminUser a) {
        try {
            String sql = "INSERT INTO AdminUser(username, password, fullname) VALUES (?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, a.getUsername());
            stm.setString(2, a.getPassword());
            stm.setString(3, a.getFullname());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
