package dal;

import model.RequestForLeave;
import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestForLeaveDBContext extends DBContext<RequestForLeave> {
// Trong RequestForLeaveDBContext.java

    public ArrayList<RequestForLeave> listForEmployee(Employee employee) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();

        // Lấy các đơn của chính mình HOẶC (đơn của cấp dưới VÀ người dùng là quản lý trực tiếp của cấp dưới đó)
        String sql = """
                 SELECT r.* FROM RequestForLeave r
                 INNER JOIN Employee e ON r.created_by = e.eid
                 WHERE r.created_by = ? 
                 OR e.supervisorid = ?
                 ORDER BY r.created_time DESC""";

        // Thực hiện truy vấn, ánh xạ kết quả (ResultSet) vào ArrayList<RequestForLeave>
        // ... (logic tương tự như phương thức get đã cung cấp)
        return requests;
    }

    // Phương thức CỐT LÕI: Lọc đơn của mình HOẶC cấp dưới
    public ArrayList<RequestForLeave> listRequestsByCreatorOrSubordinates(int creatorId, List<Integer> subordinateIds) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();
        subordinateIds.add(creatorId); // Thêm ID của chính mình vào danh sách cần lọc

        String placeholder = subordinateIds.toString().replace("[", "").replace("]", "");

        try {
            String sql = "SELECT * FROM RequestForLeave WHERE created_by IN (" + placeholder + ") ORDER BY created_time DESC";
            PreparedStatement stm = connection.prepareStatement(sql);
            // Không cần setParameter nếu dùng placeholder list. Nếu dùng PreparedStatemnent an toàn hơn, cần xử lý loop
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

                // Set CreatedBy (chỉ cần ID/Tên đơn giản cho List)
                r.setCreatedBy(empDB.getEmployeeFullInfo(rs.getInt("created_by")));

                // Set ProcessedBy
                int processedById = rs.getInt("processed_by");
                if (processedById != 0) {
                    r.setProcessedBy(empDB.getEmployeeFullInfo(processedById));
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

    // Insert đơn mới
    @Override
    public void insert(RequestForLeave model) {
        try {
            String sql = "INSERT INTO RequestForLeave (created_by, created_time, [from], [to], reason, status) VALUES (?, GETDATE(), ?, ?, ?, 1)"; // status=1: In progress
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

    // Cập nhật trạng thái duyệt
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

                // Lấy người tạo
                r.setCreatedBy(empDB.getEmployeeFullInfo(rs.getInt("created_by")));

                // Lấy người xử lý
                int processedById = rs.getInt("processed_by");
                if (processedById != 0) {
                    r.setProcessedBy(empDB.getEmployeeFullInfo(processedById));
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

// Thêm phương thức để lấy ngày nghỉ cho Agenda (Dành cho ViewAgendaController)
// Trả về map để dễ dàng xử lý trong Controller
    public ArrayList<RequestForLeave> getLeaveDays(int divisionId, Date from, Date to) {
        ArrayList<RequestForLeave> leaves = new ArrayList<>();

        // Câu truy vấn lấy các đơn đã Approved của nhân viên trong phòng ban
        // trong khoảng thời gian xác định
        String sql = """
                 SELECT r.* FROM RequestForLeave r
                 INNER JOIN Employee e ON r.created_by = e.eid
                 WHERE e.did = ? 
                 AND r.status = 2 -- APPROVED
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
                r.setCreatedBy(empDB.getEmployeeFullInfo(rs.getInt("created_by")));
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
