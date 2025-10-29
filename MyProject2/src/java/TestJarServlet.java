import dal.EmployeeDBContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/testJar")
public class TestJarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // --- Thêm 2 dòng này ---
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        // ------------------------

        EmployeeDBContext dao = new EmployeeDBContext();
        resp.getWriter().println("Kết nối thành công tới thư viện Company! Tổng nhân viên: " + dao.listAll().size());
    }
}
