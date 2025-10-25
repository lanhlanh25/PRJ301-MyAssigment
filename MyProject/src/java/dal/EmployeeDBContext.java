package dal;

import model.Employee;
import model.Division;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.iam.User;

public class EmployeeDBContext extends DBContext<Employee> {

    private void loadUserAndRoles(Employee e) {
        if (e == null) return;
        
        UserDBContext userDB = new UserDBContext();
        RoleDBContext roleDB = new RoleDBContext();
        
        User u = userDB.getByEmployeeId(e.getId()); 
        
        if (u != null) {
            u.setRoles(roleDB.getByUserId(u.getId()));
            e.setUser(u);
            e.setActive(u.getEmployee().isActive());
        }
    }

    private Employee createEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("eid"));
        e.setName(rs.getString("ename"));

        Division d = new Division();
        d.setId(rs.getInt("did"));
        d.setName(rs.getString("dname"));
        e.setDept(d);
        
        int supervisorId = rs.getInt("supervisorid");
        if (!rs.wasNull() && supervisorId != 0) {
            Employee supervisor = new Employee();
            supervisor.setId(supervisorId);
            e.setSupervisor(supervisor);
        }
        
        loadUserAndRoles(e);

        return e;
    }

    @Override
    public Employee get(int id) {
        Employee e = null;
        String sql = """
            SELECT e.eid, e.ename, e.supervisorid, d.did, d.dname
            FROM Employee e 
            INNER JOIN Division d ON e.did = d.did
            WHERE e.eid = ?
        """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                e = createEmployeeFromResultSet(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return e;
    }

    public List<Integer> getSubordinateIds(int supervisorId) {
        List<Integer> subordinateIds = new ArrayList<>();
        String sql = "SELECT eid FROM Employee WHERE supervisorid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, supervisorId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                subordinateIds.add(rs.getInt("eid"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return subordinateIds;
    }

    public ArrayList<Employee> listByDivision(int divisionId) {
        ArrayList<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.eid, e.ename, e.supervisorid, d.did, d.dname " 
                   + "FROM Employee e INNER JOIN Division d ON e.did = d.did "
                   + "WHERE e.did = ? ORDER BY e.ename";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, divisionId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = createEmployeeFromResultSet(rs);
                employees.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return employees;
    }

    @Override
    public ArrayList<Employee> list() {
        return listAll();
    }

    public ArrayList<Employee> listAll() {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = "SELECT e.eid, e.ename, e.supervisorid, d.did, d.dname " 
                   + "FROM Employee e INNER JOIN Division d ON e.did = d.did "
                   + "ORDER BY d.dname, e.ename";
        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Employee e = createEmployeeFromResultSet(rs);
                list.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return list;
    }

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