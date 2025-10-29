package controller.role;

import dal.RoleDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ManageRoleController", urlPatterns = {"/manage/role"})
public class ManageRoleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RoleDBContext dao = new RoleDBContext();
        req.setAttribute("roles", dao.list());
        req.getRequestDispatcher("/manage_role.jsp").forward(req, resp);
    }
}
