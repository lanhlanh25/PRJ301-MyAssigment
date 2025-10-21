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

        // TÁI KIỂM TRA QUYỀN TRUY CẬP (Security Check)
        RequestForLeaveDBContext dbCheck = new RequestForLeaveDBContext();
        RequestForLeave requestToCheck = dbCheck.get(requestId);

        boolean hasSupervisor = requestToCheck.getCreatedBy().getSupervisor() != null;
        boolean isSupervisor = hasSupervisor
                && requestToCheck.getCreatedBy().getSupervisor().getId() == user.getEmployee().getId();
        boolean isInProgress = requestToCheck.getStatus() == 1;

        if (!(isSupervisor && isInProgress)) {
            resp.getWriter().println("Lỗi: Bạn không có quyền duyệt đơn này hoặc đơn đã được xử lý.");
            return;
        }

        // --- Logic xử lý chính (Đã an toàn) ---
        String action = req.getParameter("action");
        String reasonForAction = req.getParameter("reasonForAction");
        int processedBy = user.getEmployee().getId();
        int newStatus;

        if ("Approve".equals(action)) {
            newStatus = 2; // Approved
        } else if ("Reject".equals(action)) {
            newStatus = 3; // Rejected
        } else {
            resp.getWriter().println("Hành động không hợp lệ!");
            return;
        }

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        db.updateStatus(requestId, newStatus, processedBy, reasonForAction);

        resp.sendRedirect("list");
    }
// Trong ReviewController.java

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int requestId = Integer.parseInt(req.getParameter("id"));
        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        RequestForLeave request = db.get(requestId);

        if (request == null) {
            resp.getWriter().println("Đơn không tồn tại.");
            return;
        }

        int currentManagerId = user.getEmployee().getId();

        // --- Bắt đầu Logic Phê Duyệt Theo Cấp Bậc ---
        // 1. Kiểm tra: Người tạo đơn phải có Supervisor (Không phải cấp cao nhất)
        boolean hasSupervisor = request.getCreatedBy().getSupervisor() != null;

        // 2. Kiểm tra: Phải là quản lý TRỰC TIẾP
        boolean isSupervisor = false;
        if (hasSupervisor) {
            // ID của người duyệt (user hiện tại) phải khớp với Supervisor ID của người tạo đơn
            isSupervisor = request.getCreatedBy().getSupervisor().getId() == currentManagerId;
        }

        // 3. Kiểm tra Trạng thái: Đơn phải ở trạng thái In progress (1)
        boolean isInProgress = request.getStatus() == 1;

        // Quyền duyệt chỉ được cấp nếu: Là quản lý trực tiếp VÀ đơn đang chờ xử lý.
        boolean canReview = isSupervisor && isInProgress;

        // Kiểm tra truy cập: Ngay cả khi không duyệt được (ví dụ đã Approved), 
        // người dùng (nếu là quản lý) vẫn phải được xem.
        // Logic này phải được đảm bảo bởi ListController (chỉ thấy đơn của mình và cấp dưới)
        req.setAttribute("request", request);
        req.setAttribute("canReview", canReview);

        req.getRequestDispatcher("/view/request/review_request.jsp").forward(req, resp);
    }
}
