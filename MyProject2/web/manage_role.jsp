<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Role"%>
<%
ArrayList<Role> roles = (ArrayList<Role>) request.getAttribute("roles");
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Quản lý vai trò</title></head>
<body>
<h2>Danh sách Role</h2>
<table border="1" cellpadding="5">
<tr><th>ID</th><th>Tên vai trò</th></tr>
<% for(Role r : roles){ %>
<tr><td><%=r.getRoleId()%></td><td><%=r.getRoleName()%></td></tr>
<% } %>
</table>
</body>
</html>
