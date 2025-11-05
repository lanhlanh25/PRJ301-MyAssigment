package dal;

import java.sql.*;
import java.util.ArrayList;
import model.Feature;

public class FeatureDBContext extends DBContext {
    public ArrayList<Feature> list() {
        ArrayList<Feature> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Feature";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Feature f = new Feature();
                f.setFeatureId(rs.getInt("feature_id"));
                f.setUrl(rs.getString("url"));
                f.setDescription(rs.getString("description"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
