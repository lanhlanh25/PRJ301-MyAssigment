package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Division;
import model.BaseModel;

public class DivisionDBContext extends DBContext<Division> {

    @Override
    public ArrayList<Division> list() {
        ArrayList<Division> divisions = new ArrayList<>();
        String sql = "SELECT did, dname FROM Division ORDER BY dname";
        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Division d = new Division();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                divisions.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DivisionDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return divisions;
    }

    @Override
    public Division get(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(Division model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Division model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Division model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}