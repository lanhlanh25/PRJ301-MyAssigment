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

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        EmployeeDBContext empDB = new EmployeeDBContext();
        RoleDBContext roleDB = new RoleDBContext();

        ArrayList<Employee> employees;

        if (isDirector(currentUser)) {
            employees = empDB.listAll();
        } 
        else {
            int deptId = currentUser.getEmployee().getDept().getId();
            employees = empDB.listByDivision(deptId);
        }

        ArrayList<Role> roles = roleDB.list();

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

        UserDBContext userDB = new UserDBContext();
        
        String action = req.getParameter("action");
        int uid = Integer.parseInt(req.getParameter("uid"));
        
        if (!isDirector(currentUser)) {
            int currentDept = currentUser.getEmployee().getDept().getId();
            int targetDept = userDB.getDivisionIdByUser(uid);

            if (currentDept != targetDept) {
                req.setAttribute("error", "❌ Bạn không có quyền thay đổi nhân viên ở phòng khác!");
                doGet(req, resp);
                return;
            }
        }
        
        if ("promote".equals(action)) {
            int newRoleId = Integer.parseInt(req.getParameter("newRoleId"));
            userDB.updateUserRole(uid, newRoleId);
            req.setAttribute("message", "✅ Cập nhật chức vụ thành công!");
        } else if ("deactivate".equals(action)) {
            userDB.deactivateEmployee(uid, false);
            req.setAttribute("message", "✅ Sa thải nhân viên thành công. Tài khoản đã bị vô hiệu hóa!");
        } else if ("activate".equals(action)) {
            userDB.deactivateEmployee(uid, true);
            req.setAttribute("message", "✅ Kích hoạt lại tài khoản thành công!");
        } else {
            req.setAttribute("error", "Hành động không hợp lệ.");
        }

        doGet(req, resp);
    }

    private boolean isDirector(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getId() == 1);
    }
}