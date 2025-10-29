<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Role, model.Feature"%>
<%
ArrayList<Role> roles = (ArrayList<Role>) request.getAttribute("roles");
ArrayList<Feature> features = (ArrayList<Feature>) request.getAttribute("features");
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Gán Feature cho Role</title></head>
<body>
<h2>Gán Feature cho Role</h2>
<form method="POST" action="feature">
    <label>Chọn Role:</label>
    <select name="role_id">
        <% for(Role r : roles){ %>
        <option value="<%=r.getRoleId()%>"><%=r.getRoleName()%></option>
        <% } %>
    </select><br><br>

    <label>Chọn Feature:</label>
    <select name="feature_id">
        <% for(Feature f : features){ %>
        <option value="<%=f.getFeatureId()%>"><%=f.getDescription()%></option>
        <% } %>
    </select><br><br>

    <input type="submit" value="Gán chức năng">
</form>
</body>
</html>
