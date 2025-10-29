<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.AdminUser"%>
<%
    ArrayList<AdminUser> users = (ArrayList<AdminUser>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>
</head>
<body>
<h2>Danh sách người dùng</h2>
<table border="1" cellpadding="5">
<tr><th>ID</th><th>Username</th><th>Fullname</th></tr>
<% for(AdminUser u : users){ %>
<tr>
<td><%=u.getAdminId()%></td>
<td><%=u.getUsername()%></td>
<td><%=u.getFullname()%></td>
</tr>
<% } %>
</table>
</body>
</html>
