package dal;

import java.util.ArrayList;
import model.iam.User;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Division;
import model.Employee;

public class UserDBContext extends DBContext<User> {

    // ===== Lấy user theo username + password (login) =====
    public User get(String username, String password) {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = """
                SELECT
                    u.uid, u.username, u.[password], u.displayname,
                    e.eid, e.ename, e.supervisorid,
                    d.did, d.dname
                FROM [User] u
                INNER JOIN [Enrollment] en ON u.[uid] = en.[uid]
                INNER JOIN [Employee] e ON e.eid = en.eid
                INNER JOIN [Division] d ON e.did = d.did
                WHERE u.username = ? AND u.[password] = ? AND en.active = 1
            """;

            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            rs = stm.executeQuery();

            if (rs.next()) {
                User u = new User();

                // --- User info ---
                u.setId(rs.getInt("uid"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setDisplayname(rs.getString("displayname"));

                // --- Division info ---
                Division d = new Division();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));

                // --- Employee info ---
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                e.setDept(d);

                int supervisorId = rs.getInt("supervisorid");
                if (rs.wasNull() || supervisorId == 0) {
                    e.setSupervisor(null);
                } else {
                    Employee supervisor = new Employee();
                    supervisor.setId(supervisorId);
                    e.setSupervisor(supervisor);
                }

                u.setEmployee(e);
                return u;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    // ===== Cập nhật thông tin User (ProfileController dùng hàm này) =====
    public void updateUser(User user) {
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE [User] SET username = ?, password = ?, displayname = ? WHERE uid = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getDisplayname());
            stm.setInt(4, user.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stm != null) stm.close();
                closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    // ===== Dùng cho chức năng Quản lý nhân viên / Thăng chức =====
    public void updateUserRole(int uid, int newRoleId) {
        String sql = "UPDATE [UserRole] SET rid = ? WHERE uid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, newRoleId);
            stm.setInt(2, uid);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public int getDivisionIdByUser(int uid) {
        String sql = """
            SELECT d.did 
            FROM [User] u
            INNER JOIN [Enrollment] en ON u.uid = en.uid
            INNER JOIN [Employee] e ON en.eid = e.eid
            WHERE u.uid = ?
        """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, uid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("did");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return -1;
    }

    // ===== Các phương thức override từ DBContext =====
    @Override
    public ArrayList<User> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User get(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(User model) {
        // gọi lại hàm updateUser để đồng bộ với DBContext
        updateUser(model);
    }

    @Override
    public void delete(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
