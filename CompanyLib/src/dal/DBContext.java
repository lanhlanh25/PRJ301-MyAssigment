package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BaseModel;

/**
 * Generic Database Context
 * @param <T> model class kế thừa từ BaseModel
 */
public abstract class DBContext<T extends BaseModel> {

    protected Connection connection = null;

    // ===== Kết nối database =====
    public DBContext() {
        try {
            String user = "sa";
            String pass = "123";
            
            String url = "jdbc:sqlserver://localhost:1433;databaseName=MyProject;encrypt=true;trustServerCertificate=true;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ===== Đóng kết nối =====
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ===== CRUD methods (bắt buộc override ở lớp con) =====
    public abstract ArrayList<T> list();

    public abstract T get(int id);

    public abstract void insert(T model);

    public abstract void update(T model);   // ✅ Quan trọng: giờ là abstract, không fix cho User nữa

    public abstract void delete(T model);
}
