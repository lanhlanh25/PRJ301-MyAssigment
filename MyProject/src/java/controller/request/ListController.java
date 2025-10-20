package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.EmployeeDBContext;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.RequestForLeave;
import model.iam.Role;
import model.iam.User;

@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
    }
// Trong ListController.java

@Override
protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
    int currentEmployeeId = user.getEmployee().getId();
    List<Integer> subordinateIds = new ArrayList<>();
    
    // Kiểm tra Role: Nếu người dùng có vai trò là Quản lý/Trưởng nhóm (Role ID 1 hoặc 2)
    boolean isManager = false;
    for (Role role : user.getRoles()) {
        // Giả định Role ID 1 (IT Head) và 2 (IT PM/Trưởng nhóm) là quản lý
        if (role.getId() == 1 || role.getId() == 2) { 
            isManager = true;
            break;
        }
    }
    
    if (isManager) {
        // 1. Nếu là Quản lý: Lấy danh sách ID cấp dưới (để xem đơn của họ)
        EmployeeDBContext empDB = new EmployeeDBContext();
        subordinateIds = empDB.getSubordinateIds(currentEmployeeId);
        // Lưu ý: subordinateIds hiện tại chỉ có ID CẤP DƯỚI TRỰC TIẾP
    } 
    // Nếu không phải quản lý (Nhân viên), subordinateIds sẽ là danh sách rỗng, 
    // và RequestForLeaveDBContext.listRequestsByCreatorOrSubordinates sẽ chỉ trả về đơn của chính mình.

    // 2. Lấy danh sách Request của mình VÀ cấp dưới (nếu có)
    RequestForLeaveDBContext reqDB = new RequestForLeaveDBContext();
    ArrayList<RequestForLeave> requests = reqDB.listRequestsByCreatorOrSubordinates(currentEmployeeId, subordinateIds);

    req.setAttribute("requests", requests);
    req.getRequestDispatcher("/view/request/list_request.jsp").forward(req, resp);
}
}