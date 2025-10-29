package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRoleFeatureDBContext extends DBContext {

    // --- Hàm gán Feature cho Role ---
    public void assignFeature(int roleId, int featureId) {
        try {
            String sql = "INSERT INTO RoleFeature(role_id, feature_id) VALUES (?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, roleId);
            stm.setInt(2, featureId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- (nếu chưa có, thêm luôn hàm assignRole cho đầy đủ) ---
    public void assignRole(int adminId, int roleId) {
        try {
            String sql = "INSERT INTO AdminUserRole(admin_id, role_id) VALUES (?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, adminId);
            stm.setInt(2, roleId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
