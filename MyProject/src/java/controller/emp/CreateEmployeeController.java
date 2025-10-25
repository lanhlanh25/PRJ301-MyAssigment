package controller.emp;

import dal.DivisionDBContext;
import dal.RoleDBContext;
import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/register")
public class CreateEmployeeController extends HttpServlet {

    private static final int PENDING_ROLE_ID = 4;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String ename = req.getParameter("ename");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        // Kiểm tra did có thể bị null nếu người dùng không chọn
        String didParam = req.getParameter("did");
        if (didParam == null || didParam.isEmpty()) {
             req.setAttribute("error", "❌ Lỗi: Vui lòng chọn Phòng Ban.");
            doGet(req, resp);
            return;
        }
        int did = Integer.parseInt(didParam);
        
        Integer supervisorid = null; // Luôn là NULL khi tự đăng ký
        boolean active = true;

        UserDBContext userDB = new UserDBContext();
        
        try {
            // KIỂM TRA TỒN TẠI TRƯỚC KHI GIAO DỊCH
            if (userDB.isUsernameExists(username)) {
                req.setAttribute("error", "❌ Lỗi: Tên đăng nhập (Username) này đã được sử dụng. Vui lòng chọn tên khác.");
                doGet(req, resp);
                return;
            }
            // KHÔNG kiểm tra ename tồn tại vì nhiều nhân viên có thể trùng tên

            userDB.insertNewEmployee(ename, username, password, did, supervisorid, PENDING_ROLE_ID, active);
            
            req.setAttribute("message", "✅ Đăng ký thành công! Vui lòng chờ Giám đốc duyệt và phân công vai trò chính thức.");
            req.getRequestDispatcher("/view/auth/message.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            // Lỗi DB (Ví dụ: Trùng Ename nếu có ràng buộc UNIQUE, hoặc lỗi IDENTITY)
            req.setAttribute("error", "❌ Lỗi: Không thể đăng ký do lỗi hệ thống CSDL. Vui lòng kiểm tra lại dữ liệu hoặc liên hệ quản trị viên.");
            doGet(req, resp);
        } catch (Exception e) {
            // Các lỗi khác (ví dụ: NumberFormatException nếu didParam không hợp lệ)
            req.setAttribute("error", "❌ Lỗi: Xảy ra lỗi không xác định. Vui lòng thử lại.");
            doGet(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        DivisionDBContext divDB = new DivisionDBContext();
        
        // Chỉ cần load danh sách phòng ban
        req.setAttribute("divisions", divDB.list());

        // Chuyển đến form đăng ký
        req.getRequestDispatcher("/view/auth/register.jsp").forward(req, resp);
    }
}