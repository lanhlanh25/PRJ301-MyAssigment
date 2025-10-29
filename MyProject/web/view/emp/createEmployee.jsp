<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <title>Tạo Tài Khoản Nhân Viên Mới</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(120deg, #fef6f0, #f4f1ff); color: #444; min-height: 100vh; display: flex; justify-content: center; align-items: center; }
        .container { background-color: #ffffff; border-radius: 16px; box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); width: 500px; padding: 40px 45px; animation: fadeIn 0.5s ease-in-out; }
        h1 { text-align: center; color: #a682ff; margin-bottom: 25px; }
        .message.success { color: green; font-weight: bold; text-align: center; margin-bottom: 15px; }
        .message.error { color: red; font-weight: bold; text-align: center; margin-bottom: 15px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: 600; color: #555; }
        input[type="text"], input[type="password"], select { width: 100%; padding: 10px 12px; border-radius: 8px; border: 1px solid #d8d8d8; background-color: #fafafa; font-size: 1em; }
        input[type="submit"] { background-color: #c3a6ff; color: white; border: none; border-radius: 8px; padding: 12px; font-weight: bold; cursor: pointer; transition: background-color 0.3s ease; }
        input[type="submit"]:hover { background-color: #a682ff; }
        .back-link { text-align: center; margin-top: 15px; }
        .back-link a { color: #a682ff; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="container">
        <h1>👤 Tạo Tài Khoản Nhân Viên</h1>

        <c:if test="${not empty message}">
            <div class="message success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <form action="create" method="POST">
            <label for="ename">Tên Nhân Viên:</label>
            <input type="text" id="ename" name="ename" required placeholder="Ví dụ: Nguyễn Văn A">

            <label for="username">Tên Đăng Nhập:</label>
            <input type="text" id="username" name="username" required placeholder="Ví dụ: nva_it">

            <label for="password">Mật Khẩu:</label>
            <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu ban đầu">

            <label for="did">Phòng Ban:</label>
            <select id="did" name="did" required>
                <option value="" disabled selected>Chọn Phòng Ban</option>
                <c:forEach var="division" items="${requestScope.divisions}">
                    <option value="${division.id}">${division.name}</option>
                </c:forEach>
            </select>

            <label for="rid">Chức Vụ / Vai Trò (Role):</label>
            <select id="rid" name="rid" required>
                <option value="" disabled selected>Chọn Chức Vụ</option>
                <c:forEach var="role" items="${requestScope.roles}">
                    <option value="${role.id}">${role.name}</option>
                </c:forEach>
            </select>
            
            <label for="supervisorid">Người Quản Lý Trực Tiếp (Supervisor):</label>
            <select id="supervisorid" name="supervisorid">
                <option value="">Không có Supervisor (Sếp Tổng)</option>
                <c:forEach var="employee" items="${requestScope.supervisors}">
                    <option value="${employee.id}">${employee.name} (${employee.dept.name})</option>
                </c:forEach>
            </select>

            <input type="submit" value="Tạo Tài Khoản">
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/employee/manage">← Quay về Quản Lý Nhân Viên</a>
        </div>
    </div>
</body>
</html>