package dal;

import java.util.ArrayList;
import model.iam.Role;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.iam.Feature;

public class RoleDBContext extends DBContext<Role> {

    public ArrayList<Role> getByUserId(int uid) {
        ArrayList<Role> roles = new ArrayList<>();

        String sql = """
            SELECT 
                r.rid, r.rname, f.fid, f.url
            FROM [User] u 
            INNER JOIN [UserRole] ur ON u.uid = ur.uid
            INNER JOIN [Role] r ON r.rid = ur.rid
            LEFT JOIN [RoleFeature] rf ON rf.rid = r.rid
            LEFT JOIN [Feature] f ON f.fid = rf.fid
            WHERE u.uid = ?
            ORDER BY r.rid
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, uid);
            ResultSet rs = stm.executeQuery();

            Role current = null;
            int lastRoleId = -1;

            while (rs.next()) {
                int rid = rs.getInt("rid");
                if (rid != lastRoleId) {
                    current = new Role();
                    current.setId(rid);
                    current.setName(rs.getString("rname"));
                    roles.add(current);
                    lastRoleId = rid;
                }

                int fid = rs.getInt("fid");
                if (!rs.wasNull()) {
                    Feature f = new Feature();
                    f.setId(fid);
                    f.setUrl(rs.getString("url"));
                    current.getFeatures().add(f);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return roles;
    }

    @Override
    public ArrayList<Role> list() {
        ArrayList<Role> roles = new ArrayList<>();
        String sql = "SELECT rid, rname FROM [Role]";
        try (PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Role r = new Role();
                r.setId(rs.getInt("rid"));
                r.setName(rs.getString("rname"));
                roles.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return roles;
    }

    @Override
    public Role get(int id) {
        Role r = null;
        String sql = "SELECT rid, rname FROM [Role] WHERE rid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                r = new Role();
                r.setId(rs.getInt("rid"));
                r.setName(rs.getString("rname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return r;
    }

    @Override
    public void insert(Role model) {
        String sql = "INSERT INTO [Role](rname) VALUES(?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, model.getName());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Role model) {
        String sql = "UPDATE [Role] SET rname = ? WHERE rid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, model.getName());
            stm.setInt(2, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Role model) {
        String sql = "DELETE FROM [Role] WHERE rid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }
}
