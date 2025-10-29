package controller.feature;

import dal.FeatureDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ManageFeatureController", urlPatterns = {"/manage/feature"})
public class ManageFeatureController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FeatureDBContext dao = new FeatureDBContext();
        req.setAttribute("features", dao.list());
        req.getRequestDispatcher("/manage_feature.jsp").forward(req, resp);
    }
}
