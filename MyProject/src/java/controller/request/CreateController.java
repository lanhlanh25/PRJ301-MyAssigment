package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Lấy thông tin từ form
        Date fromDate = Date.valueOf(req.getParameter("fromDate"));
        Date toDate = Date.valueOf(req.getParameter("toDate"));
        String reason = req.getParameter("reason");

        // Tạo đối tượng RequestForLeave
        RequestForLeave r = new RequestForLeave();
        r.setFromDate(fromDate);
        r.setToDate(toDate);
        r.setReason(reason);
        r.setCreatedBy(user.getEmployee()); // Lấy Employee đã được gán khi login

        // Lưu vào CSDL
        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        db.insert(r);

        // Chuyển hướng
        resp.sendRedirect("list");
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Đã có user (Authentication) và quyền (Authorization)
        req.getRequestDispatcher("/view/request/create_request.jsp").forward(req, resp);
    }
}