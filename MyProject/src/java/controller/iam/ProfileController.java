package controller.iam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.iam.User;
import dal.UserDBContext;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Bảo đảm hiển thị tiếng Việt đúng
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (User) (session != null ? session.getAttribute("auth") : null);

        // Nếu chưa đăng nhập thì chuyển về trang đăng nhập
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/view/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("auth") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        User user = (User) session.getAttribute("auth");

        // Lấy dữ liệu từ form
        String displayname = req.getParameter("displayname");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Cập nhật vào object User hiện tại
        user.setDisplayname(displayname);
        user.setUsername(username);
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password.trim());
        }

        // Gọi DBContext cập nhật database
        UserDBContext db = new UserDBContext();
        db.update(user);

        // Cập nhật lại session
        session.setAttribute("auth", user);

        // Gửi thông báo
        req.setAttribute("message", "✅ Cập nhật thành công!");
        req.setAttribute("messageType", "success");

        req.getRequestDispatcher("/view/profile.jsp").forward(req, resp);
    }
}
