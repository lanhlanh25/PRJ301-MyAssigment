package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/review")
public class ReviewController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int requestId = Integer.parseInt(req.getParameter("requestId"));
        String action = req.getParameter("action"); // 'Approve' hoặc 'Reject'
        String reasonForAction = req.getParameter("reasonForAction");
        int processedBy = user.getEmployee().getId();
        int newStatus;

        if ("Approve".equals(action)) {
            newStatus = 2; // Approved
        } else if ("Reject".equals(action)) {
            newStatus = 3; // Rejected
        } else {
            resp.getWriter().println("Invalid action!");
            return;
        }

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        db.updateStatus(requestId, newStatus, processedBy, reasonForAction);

        // Chuyển hướng về List
        resp.sendRedirect("list");
    }
// Trong ReviewController.java
@Override
protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
    int requestId = Integer.parseInt(req.getParameter("id"));
    RequestForLeaveDBContext db = new RequestForLeaveDBContext();
    RequestForLeave request = db.get(requestId);
    
    if (request == null) {
        req.setAttribute("message", "Đơn không tồn tại.");
        req.getRequestDispatcher("/view/auth/message.jsp").forward(req, resp);
        return;
    }

    int currentManagerId = user.getEmployee().getId();
    
    // 1. Kiểm tra quyền duyệt (Điều kiện 1: Phải là quản lý trực tiếp)
    boolean isSupervisor = false;
    if (request.getCreatedBy().getSupervisor() != null) {
        // ID của người duyệt (user hiện tại) phải khớp với Supervisor ID của người tạo đơn
        isSupervisor = request.getCreatedBy().getSupervisor().getId() == currentManagerId;
    }
    
    // 2. Kiểm tra trạng thái (Điều kiện 2: Đơn phải ở trạng thái In progress).
    boolean isInProgress = request.getStatus() == 1; 

    // Biến cờ quan trọng: Chỉ có quyền xem VÀ duyệt nếu là quản lý trực tiếp VÀ đơn chưa được xử lý
    boolean canReview = isSupervisor && isInProgress; 

    // Quan trọng: Nếu không phải quản lý trực tiếp, nhưng vẫn là nhân viên trong công ty,
    // họ vẫn có thể xem chi tiết đơn của chính mình (nếu status != In progress) 
    // nhưng KHÔNG được phép thay đổi trạng thái.
    
    req.setAttribute("request", request);
    req.setAttribute("canReview", canReview); // Dùng để ẩn/hiện nút Approve/Reject trên JSP
    
    req.getRequestDispatcher("/view/request/review_request.jsp").forward(req, resp);
}
}