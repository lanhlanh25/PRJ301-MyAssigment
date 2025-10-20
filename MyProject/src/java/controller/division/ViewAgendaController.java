// Trong ViewAgendaController.java
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
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Lấy thông tin phòng ban của Quản lý hiện tại
        int divisionId = user.getEmployee().getDept().getId();
        
        // 1. Xử lý khoảng thời gian (Lấy tham số từ request, hoặc mặc định 7 ngày)
        Date from;
        Date to;
        
        try {
            from = Date.valueOf(req.getParameter("from"));
            to = Date.valueOf(req.getParameter("to"));
        } catch (Exception e) {
            // Mặc định: Từ ngày hiện tại đến 7 ngày sau (nếu không có tham số)
            Calendar cal = Calendar.getInstance();
            from = new Date(cal.getTimeInMillis());
            cal.add(Calendar.DAY_OF_YEAR, 7);
            to = new Date(cal.getTimeInMillis());
        }

        // 2. Lấy danh sách nhân viên trong phòng ban
        EmployeeDBContext empDB = new EmployeeDBContext();
        // CẦN BỔ SUNG: EmployeeDBContext.getEmployeesByDivision(int divisionId)
        // Hiện tại, tạm thời lấy danh sách cấp dưới trực tiếp (vì không có phương thức lấy tất cả)
        List<Employee> allEmployees = new ArrayList<>(); // empDB.getEmployeesByDivision(divisionId); 

        // 3. Lấy danh sách các đơn nghỉ phép đã Approved trong khoảng thời gian
        RequestForLeaveDBContext reqDB = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> approvedLeaves = reqDB.getLeaveDays(divisionId, from, to);
        
        // 4. Set Attribute và Forward
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        req.setAttribute("employees", allEmployees);
        req.setAttribute("approvedLeaves", approvedLeaves);
        
        req.getRequestDispatcher("/view/division/agenda.jsp").forward(req, resp);
    }
}