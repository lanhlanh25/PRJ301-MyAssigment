<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.AdminUser"%>
<%
    AdminUser admin = (AdminUser) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body {font-family: Segoe UI; background-color: #fafafa; margin: 0;}
        .navbar {background-color: #009688; color: white; padding: 12px; display: flex; justify-content: space-between;}
        .navbar a {color: white; text-decoration: none; margin-right: 15px;}
        .container {padding: 20px;}
        table {border-collapse: collapse; width: 100%;}
        table, th, td {border: 1px solid #ccc;}
        th, td {padding: 10px;}
    </style>
</head>
<body>
    <div class="navbar">
        <div>Xin chào, <b><%= admin.getFullname() %></b></div>
        <div><a href="logout">Đăng xuất</a></div>
    </div>
    <div class="container">
        <h2>Trang quản trị hệ thống eLeave</h2>
        <ul>
            <li><a href="manage_user.jsp">Quản lý người dùng</a></li>
            <li><a href="manage_role.jsp">Quản lý vai trò</a></li>
            <li><a href="manage_feature.jsp">Quản lý chức năng</a></li>
        </ul>
    </div>
</body>
</html>
