package controller.user;

import dal.AdminUserDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ManageUserController", urlPatterns = {"/manage/user"})
public class ManageUserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AdminUserDBContext dao = new AdminUserDBContext();
        req.setAttribute("users", dao.list());
        req.getRequestDispatcher("/manage_user.jsp").forward(req, resp);
    }
}
