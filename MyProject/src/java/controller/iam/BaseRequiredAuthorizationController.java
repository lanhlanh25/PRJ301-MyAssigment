package controller.iam;

import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.iam.Feature;
import model.iam.Role;
import model.iam.User;

public abstract class BaseRequiredAuthorizationController extends BaseRequiredAuthenticationController {

    private boolean isAuthorized(HttpServletRequest req, User user) {
        
        // 1. Khởi tạo/Lấy Map Features
        Map<String, Boolean> authorizedFeatures = new HashMap<>();
        boolean authorized = false;
        
        // 2. Nạp Roles nếu chưa có
        if (user.getRoles().isEmpty()) {
            RoleDBContext db = new RoleDBContext();
            user.setRoles(db.getByUserId(user.getId()));
        }
        
        String url = req.getServletPath();
        
        // 3. Duyệt qua Roles và Features để xây dựng Map và kiểm tra quyền truy cập hiện tại
        for (Role role : user.getRoles()) {
            for (Feature feature : role.getFeatures()) {
                authorizedFeatures.put(feature.getUrl(), true); // Bổ sung quyền vào Map
                
                if(feature.getUrl().equals(url))
                    authorized = true; // Kiểm tra quyền cho URL hiện tại
            }
        }
        
        // 4. Lưu trữ Map quyền vào User và Session
        user.setAuthorizedFeatures(authorizedFeatures);
        // Lưu ý: Cập nhật session là cần thiết vì user object đã bị thay đổi
        req.getSession().setAttribute("auth", user); 

        // Nếu người dùng đang truy cập /home, họ luôn được cho phép nếu đã xác thực
        if(url.equals("/home")) return true;

        return authorized;
    }

    protected abstract void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;
    protected abstract void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException ;
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        if(isAuthorized(req, user))
            processPost(req, resp, user);
        else
            resp.getWriter().println("access denied!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        if(isAuthorized(req, user))
            processGet(req, resp, user);
        else
            resp.getWriter().println("access denied!");
    }
}