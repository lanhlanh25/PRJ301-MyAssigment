// Trong controller.iam/LogoutController.java
package controller.iam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Xóa Session (Session Invalidation) hoặc xóa Attribute "auth"
        req.getSession().removeAttribute("auth");
        // Chuyển hướng về trang đăng nhập
        resp.sendRedirect("index.html"); 
    }
}