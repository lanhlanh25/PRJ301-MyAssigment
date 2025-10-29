package controller.role;

import dal.AdminUserDBContext;
import dal.RoleDBContext;
import dal.UserRoleFeatureDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AssignRoleController", urlPatterns = {"/assign/role"})
public class AssignRoleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AdminUserDBContext userDAO = new AdminUserDBContext();
        RoleDBContext roleDAO = new RoleDBContext();

        req.setAttribute("users", userDAO.list());
        req.setAttribute("roles", roleDAO.list());
        req.getRequestDispatcher("/assign_role.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int adminId = Integer.parseInt(req.getParameter("admin_id"));
        int roleId = Integer.parseInt(req.getParameter("role_id"));

        UserRoleFeatureDBContext dao = new UserRoleFeatureDBContext();
        dao.assignRole(adminId, roleId);

        resp.sendRedirect("assign/role");
    }
}
