package dal;

import model.Employee;
import model.Division;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDBContext extends DBContext<Employee> {

    public Employee getEmployeeFullInfo(int eid) {
        try {
            String sql = """
                         SELECT e.eid, e.ename, e.supervisorid, d.did, d.dname
                         FROM Employee e INNER JOIN Division d ON e.did = d.did
                         WHERE e.eid = ?""";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));

                // Division Info
                Division d = new Division();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);

                // Supervisor Info (Tái sử dụng hàm này để lấy thông tin quản lý)
                int supervisorId = rs.getInt("supervisorid");
                if (supervisorId != 0) {
                    Employee supervisor = new Employee();
                    supervisor.setId(supervisorId);
                    e.setSupervisor(supervisor);
                }

                return e;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return null;
    }

    // Phương thức CỐT LÕI: Lấy danh sách ID cấp dưới
    public List<Integer> getSubordinateIds(int supervisorId) {
        List<Integer> subordinateIds = new ArrayList<>();
        try {
            String sql = "SELECT eid FROM Employee WHERE supervisorid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, supervisorId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                subordinateIds.add(rs.getInt("eid"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Lưu ý: Không đóng kết nối nếu phương thức này được gọi cùng với các phương thức khác.
            // Nếu bạn muốn đóng, hãy xử lý try-with-resources hoặc kiểm soát việc đóng kết nối
            // ở lớp Controller/Service. (Ở đây tôi giữ logic cũ của bạn là đóng ở finally)
            closeConnection();
        }
        return subordinateIds;
    }
// Trong dal/EmployeeDBContext.java

// Phương thức mới: Lấy danh sách nhân viên theo phòng ban (Division Leader Agenda)
    public List<Employee> getEmployeesByDivision(int divisionId) {
        
        List<Employee> employees = new ArrayList<>();
        try {
            String sql = "SELECT eid, ename FROM Employee WHERE did = ? ORDER BY ename";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, divisionId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                // Lưu ý: Không cần lấy full info (Division/Supervisor) vì List Agenda không cần
                employees.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cần xử lý đóng kết nối nếu đây là lần cuối dùng DB
            // (Hoặc giữ nguyên cách bạn quản lý connection)
            // closeConnection(); 
        }
        return employees;
        
    }

    @Override
    public ArrayList<Employee> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Employee get(int id) {
        return getEmployeeFullInfo(id);
    } // Tái sử dụng

    @Override
    public void insert(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
