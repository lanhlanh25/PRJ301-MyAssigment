package dal;

import model.RequestForLeave;
import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestForLeaveDBContext extends DBContext<RequestForLeave> {

    public ArrayList<RequestForLeave> listForEmployee(Employee employee) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();

        String sql = """
                 SELECT r.* FROM RequestForLeave r
                 INNER JOIN Employee e ON r.created_by = e.eid
                 WHERE r.created_by = ? 
                 OR e.supervisorid = ?
                 ORDER BY r.created_time DESC""";

        return requests;
    }

    public ArrayList<RequestForLeave> listRequestsByCreatorOrSubordinates(int creatorId, List<Integer> subordinateIds) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();
        subordinateIds.add(creatorId); 

        String placeholder = subordinateIds.toString().replace("[", "").replace("]", "");

        try {
            String sql = "SELECT * FROM RequestForLeave WHERE created_by IN (" + placeholder + ") ORDER BY created_time DESC";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            EmployeeDBContext empDB = new EmployeeDBContext();
            while (rs.next()) {
                RequestForLeave r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setFromDate(rs.getDate("from"));
                r.setToDate(rs.getDate("to"));
                r.setReason(rs.getString("reason"));
                r.setCreatedTime(rs.getTimestamp("created_time"));
                r.setStatus(rs.getInt("status"));
                r.setReasonForAction(rs.getString("reason_for_action"));

                r.setCreatedBy(empDB.get(rs.getInt("created_by")));

                int processedById = rs.getInt("processed_by");
                if (processedById != 0) {
                    r.setProcessedBy(empDB.get(processedById));
                }

                requests.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return requests;
    }

    @Override
    public void insert(RequestForLeave model) {
        try {
            String sql = "INSERT INTO RequestForLeave (created_by, created_time, [from], [to], reason, status) VALUES (?, GETDATE(), ?, ?, ?, 1)"; 
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getCreatedBy().getId());
            stm.setDate(2, model.getFromDate());
            stm.setDate(3, model.getToDate());
            stm.setString(4, model.getReason());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public void updateStatus(int requestId, int newStatus, int processedBy, String reasonForAction) {
        try {
            String sql = "UPDATE RequestForLeave SET status = ?, processed_by = ?, processed_time = GETDATE(), reason_for_action = ? WHERE rid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, newStatus);
            stm.setInt(2, processedBy);
            stm.setString(3, reasonForAction);
            stm.setInt(4, requestId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<RequestForLeave> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestForLeave get(int id) {
        try {
            String sql = "SELECT * FROM RequestForLeave WHERE rid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                RequestForLeave r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setFromDate(rs.getDate("from"));
                r.setToDate(rs.getDate("to"));
                r.setReason(rs.getString("reason"));
                r.setCreatedTime(rs.getTimestamp("created_time"));
                r.setStatus(rs.getInt("status"));
                r.setReasonForAction(rs.getString("reason_for_action"));

                EmployeeDBContext empDB = new EmployeeDBContext();

                r.setCreatedBy(empDB.get(rs.getInt("created_by")));

                int processedById = rs.getInt("processed_by");
                if (processedById != 0) {
                    r.setProcessedBy(empDB.get(processedById));
                }
                r.setProcessedTime(rs.getTimestamp("processed_time"));

                return r;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return null;
    }

    public ArrayList<RequestForLeave> getLeaveDays(int divisionId, Date from, Date to) {
        ArrayList<RequestForLeave> leaves = new ArrayList<>();

        String sql = """
                 SELECT r.* FROM RequestForLeave r
                 INNER JOIN Employee e ON r.created_by = e.eid
                 WHERE e.did = ? 
                 AND r.status = 2 
                 AND r.[from] <= ? AND r.[to] >= ?""";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, divisionId);
            stm.setDate(2, to);
            stm.setDate(3, from);
            ResultSet rs = stm.executeQuery();

            EmployeeDBContext empDB = new EmployeeDBContext();

            while (rs.next()) {
                RequestForLeave r = new RequestForLeave();
                r.setFromDate(rs.getDate("from"));
                r.setToDate(rs.getDate("to"));
                r.setCreatedBy(empDB.get(rs.getInt("created_by")));
                leaves.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return leaves;
    }

    @Override
    public void update(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}