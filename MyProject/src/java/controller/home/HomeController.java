package controller.home;

import controller.iam.BaseRequiredAuthorizationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.iam.User;

@WebServlet(urlPatterns = "/home")
// BƯỚC QUAN TRỌNG: Kế thừa BaseRequiredAuthorizationController
public class HomeController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Hiện tại không cần xử lý POST cho trang home
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Logic BaseRequiredAuthorizationController đã chạy, quyền đã được nạp vào user object.
        
        // CHUYỂN TIẾP NỘI BỘ đến file JSP
        req.getRequestDispatcher("/home/home.jsp").forward(req, resp);
    }
}