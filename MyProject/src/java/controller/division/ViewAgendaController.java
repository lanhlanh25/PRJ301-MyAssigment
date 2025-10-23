package controller.division;

import controller.iam.BaseRequiredAuthorizationController;
import dal.EmployeeDBContext;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.Employee;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/division/agenda")
public class ViewAgendaController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Trang này chỉ dùng GET → redirect nếu POST
        resp.sendRedirect(req.getContextPath() + "/division/agenda");
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        // ✅ 1. Lấy Division ID của người dùng hiện tại
        int divisionId = user.getEmployee().getDept().getId();

        // ✅ 2. Xử lý khoảng thời gian (from/to)
        Date from;
        Date to;
        try {
            String fromParam = req.getParameter("from");
            String toParam = req.getParameter("to");

            if (fromParam != null && toParam != null) {
                from = Date.valueOf(fromParam);
                to = Date.valueOf(toParam);
            } else {
                // Mặc định: Từ hôm nay → 7 ngày sau
                Calendar cal = Calendar.getInstance();
                from = new Date(cal.getTimeInMillis());
                cal.add(Calendar.DAY_OF_YEAR, 7);
                to = new Date(cal.getTimeInMillis());
            }
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            from = new Date(cal.getTimeInMillis());
            cal.add(Calendar.DAY_OF_YEAR, 7);
            to = new Date(cal.getTimeInMillis());
        }

        // ✅ 3. Lấy danh sách nhân viên trong phòng ban
        EmployeeDBContext empDB = new EmployeeDBContext();
        List<Employee> allEmployees = empDB.listByDivision(divisionId);  // <-- sửa tại đây

        // ✅ 4. Lấy danh sách các đơn nghỉ phép đã duyệt
        RequestForLeaveDBContext reqDB = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> approvedLeaves = reqDB.getLeaveDays(divisionId, from, to);

        // ✅ 5. Truyền dữ liệu sang JSP
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        req.setAttribute("employees", allEmployees);
        req.setAttribute("approvedLeaves", approvedLeaves);

        // ✅ 6. Hiển thị giao diện
        req.getRequestDispatcher("/view/division/agenda.jsp").forward(req, resp);
    }
}
