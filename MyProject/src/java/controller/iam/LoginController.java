package controller.iam;

import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.iam.User;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDBContext db = new UserDBContext();
        User u = db.get(username, password);

        if (u != null) {
            HttpSession session = req.getSession();
            session.setAttribute("auth", u);

            // SỬA: Chuyển hướng đến URL MAPPING /home để HomeController xử lý
            resp.sendRedirect("home"); 
        } else {
            req.setAttribute("message", "Login Failed! Invalid username or password.");
            req.getRequestDispatcher("view/auth/message.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
    }
}