package controller.emp;

import dal.EmployeeDBContext;
import dal.RoleDBContext;
import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.Employee;
import model.iam.Role;
import model.iam.User;

@WebServlet("/employee/manage")
public class ManageEmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("auth");

        // Nếu chưa đăng nhập → chuyển về trang login
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        // Khai báo DBContext
        EmployeeDBContext empDB = new EmployeeDBContext();
        RoleDBContext roleDB = new RoleDBContext();

        ArrayList<Employee> employees;

        // ✅ Giám đốc → xem tất cả
        if (isDirector(currentUser)) {
            employees = empDB.listAll();
        } 
        // ✅ Quản lý → chỉ xem nhân viên trong phòng ban
        else {
            int deptId = currentUser.getEmployee().getDept().getId();
            employees = empDB.listByDivision(deptId);
        }

        ArrayList<Role> roles = roleDB.list();

        // Đưa dữ liệu sang JSP
        req.setAttribute("employees", employees);
        req.setAttribute("roles", roles);

        req.getRequestDispatcher("/view/division/manageEmployee.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("auth");

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        // Lấy dữ liệu từ form
        int uid = Integer.parseInt(req.getParameter("uid"));
        int newRoleId = Integer.parseInt(req.getParameter("newRoleId"));

        UserDBContext userDB = new UserDBContext();

        // ✅ Nếu không phải giám đốc → chỉ được đổi trong cùng phòng ban
        if (!isDirector(currentUser)) {
            int currentDept = currentUser.getEmployee().getDept().getId();
            int targetDept = userDB.getDivisionIdByUser(uid);

            if (currentDept != targetDept) {
                req.setAttribute("error", "❌ Bạn không có quyền thay đổi nhân viên ở phòng khác!");
                doGet(req, resp);
                return;
            }
        }

        // ✅ Cập nhật chức vụ
        userDB.updateUserRole(uid, newRoleId);

        req.setAttribute("message", "✅ Cập nhật chức vụ thành công!");
        doGet(req, resp);
    }

    // ✅ Hàm kiểm tra xem user có phải giám đốc không
    private boolean isDirector(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getId() == 1);
    }
}
