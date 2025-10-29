package controller.feature;

import dal.RoleDBContext;
import dal.FeatureDBContext;
import dal.UserRoleFeatureDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AssignFeatureController", urlPatterns = {"/assign/feature"})
public class AssignFeatureController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RoleDBContext roleDAO = new RoleDBContext();
        FeatureDBContext featureDAO = new FeatureDBContext();

        req.setAttribute("roles", roleDAO.list());
        req.setAttribute("features", featureDAO.list());
        req.getRequestDispatcher("/assign_feature.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int roleId = Integer.parseInt(req.getParameter("role_id"));
        int featureId = Integer.parseInt(req.getParameter("feature_id"));

        UserRoleFeatureDBContext dao = new UserRoleFeatureDBContext();
        dao.assignFeature(roleId, featureId);

        resp.sendRedirect("assign/feature");
    }
}
