<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.AdminUser, model.Role"%>
<%
ArrayList<AdminUser> users = (ArrayList<AdminUser>) request.getAttribute("users");
ArrayList<Role> roles = (ArrayList<Role>) request.getAttribute("roles");
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Gán Role cho User</title></head>
<body>
<h2>Gán Role cho User</h2>
<form method="POST" action="role">
    <label>Chọn User:</label>
    <select name="admin_id">
        <% for(AdminUser u : users){ %>
        <option value="<%=u.getAdminId()%>"><%=u.getFullname()%></option>
        <% } %>
    </select><br><br>

    <label>Chọn Role:</label>
    <select name="role_id">
        <% for(Role r : roles){ %>
        <option value="<%=r.getRoleId()%>"><%=r.getRoleName()%></option>
        <% } %>
    </select><br><br>

    <input type="submit" value="Gán quyền">
</form>
</body>
</html>
