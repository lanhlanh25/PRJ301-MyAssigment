package dal;

import java.sql.*;
import java.util.ArrayList;
import model.Role;

public class RoleDBContext extends DBContext {
    public ArrayList<Role> list() {
        ArrayList<Role> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Role";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
