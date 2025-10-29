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

                u.setId(rs.getInt("uid"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setDisplayname(rs.getString("displayname"));

                Division d = new Division();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));

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

    public User getByEmployeeId(int eid) {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = """
                SELECT u.uid, u.username, u.[password], u.displayname, en.active
                FROM [User] u
                INNER JOIN [Enrollment] en ON u.[uid] = en.[uid]
                WHERE en.eid = ?
            """;

            stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            rs = stm.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("uid"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setDisplayname(rs.getString("displayname"));
                
                Employee e = new Employee();
                e.setActive(rs.getBoolean("active"));
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

    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM [User] WHERE username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return false;
    }
    
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

    public void deactivateEmployee(int uid, boolean activeStatus) {
        String sql = "UPDATE Enrollment SET active = ? WHERE uid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1, activeStatus);
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

    public void insertNewEmployee(String ename, String username, String password, int did, Integer supervisorid, int rid, boolean active) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmUser = null;
        PreparedStatement stmEmployee = null;
        PreparedStatement stmEnrollment = null;
        PreparedStatement stmUserRole = null;
        
        ResultSet rsUser = null;
        ResultSet rsEmployee = null;

        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=MyProject;encrypt=true;trustServerCertificate=true;", "sa", "123");
            conn.setAutoCommit(false); 
            
            // 1. Insert into [User] (Assumes UID is IDENTITY)
            String sqlUser = "INSERT INTO [User] (username, password, displayname) VALUES (?, ?, ?)";
            stmUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            stmUser.setString(1, username);
            stmUser.setString(2, password);
            stmUser.setString(3, ename);
            stmUser.executeUpdate();
            
            rsUser = stmUser.getGeneratedKeys();
            if (!rsUser.next()) {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            int uid = rsUser.getInt(1); 
            
            // 2. Insert into [Employee] (Assumes EID is IDENTITY)
            String sqlEmployee = "INSERT INTO [Employee] (ename, did, supervisorid) VALUES (?, ?, ?)";
            stmEmployee = conn.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS);
            stmEmployee.setString(1, ename);
            stmEmployee.setInt(2, did);
            if (supervisorid != null) {
                stmEmployee.setInt(3, supervisorid);
            } else {
                stmEmployee.setNull(3, java.sql.Types.INTEGER);
            }
            stmEmployee.executeUpdate();
            
            rsEmployee = stmEmployee.getGeneratedKeys();
            if (!rsEmployee.next()) {
                throw new SQLException("Creating employee failed, no ID obtained.");
            }
            int eid = rsEmployee.getInt(1); 

            // 3. Insert into [Enrollment]
            String sqlEnrollment = "INSERT INTO [Enrollment] (uid, eid, active) VALUES (?, ?, ?)";
            stmEnrollment = conn.prepareStatement(sqlEnrollment);
            stmEnrollment.setInt(1, uid);
            stmEnrollment.setInt(2, eid);
            stmEnrollment.setBoolean(3, active);
            stmEnrollment.executeUpdate();
            
            // 4. Insert into [UserRole]
            String sqlUserRole = "INSERT INTO [UserRole] (uid, rid) VALUES (?, ?)";
            stmUserRole = conn.prepareStatement(sqlUserRole);
            stmUserRole.setInt(1, uid);
            stmUserRole.setInt(2, rid);
            stmUserRole.executeUpdate();

            conn.commit(); 

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException rollbackEx) {
                    Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Rollback failed", rollbackEx);
                }
            }
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; 
        } finally {
            try {
                if (rsUser != null) rsUser.close();
                if (rsEmployee != null) rsEmployee.close();
                if (stmUser != null) stmUser.close();
                if (stmEmployee != null) stmEmployee.close();
                if (stmEnrollment != null) stmEnrollment.close();
                if (stmUserRole != null) stmUserRole.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Closing resources failed", e);
            }
        }
    }


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
        updateUser(model);
    }

    @Override
    public void delete(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}