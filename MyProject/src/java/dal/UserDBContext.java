
package dal;

import java.util.ArrayList;
import model.iam.User;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Division;
import model.Employee;
public class UserDBContext extends DBContext<User> {

    public User get(String username, String password) {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = """
                                 SELECT
                                 u.uid, u.username, u.displayname,
                                 e.eid, e.ename,
                                 d.did, d.dname
                                 FROM [User] u INNER JOIN [Enrollment] en ON u.[uid] = en.[uid]
                                 INNER JOIN [Employee] e ON e.eid = en.eid
                                 INNER JOIN [Division] d ON e.did = d.did
                                 WHERE
                                 u.username = ? AND u.[password] = ?
                                 AND en.active = 1""";

            // Khởi tạo PreparedStatement (stm) ngay tại đây
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);

            // Chạy truy vấn
            rs = stm.executeQuery(); // Dòng này sẽ hoạt động

            while (rs.next()) {
                User u = new User();

                // 1. Tạo đối tượng Division
                // (Đảm bảo lớp Division đã được đổi tên từ Department)
                Division d = new Division();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));

                // 2. Tạo đối tượng Employee
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                e.setDept(d); // Gán Division vào Employee

                // 3. Gán Employee vào User
                u.setEmployee(e);
                u.setUsername(username);
                u.setId(rs.getInt("uid"));
                u.setDisplayname(rs.getString("displayname"));

                return u;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Đóng ResultSet và PreparedStatement ở đây
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, e);
            }
            closeConnection();
        }
        return null;
    }

    @Override
    public ArrayList<User> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
