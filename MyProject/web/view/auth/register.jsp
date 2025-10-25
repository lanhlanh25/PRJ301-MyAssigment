<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng Ký Tài Khoản Mới</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(120deg, #fef6f0, #f4f1ff); color: #444; min-height: 100vh; display: flex; justify-content: center; align-items: center; }
        .container { background-color: #ffffff; border-radius: 16px; box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); width: 500px; padding: 40px 45px; animation: fadeIn 0.5s ease-in-out; }
        h1 { text-align: center; color: #ff8e88; margin-bottom: 25px; }
        .message.success { color: green; font-weight: bold; text-align: center; margin-bottom: 15px; }
        .message.error { color: red; font-weight: bold; text-align: center; margin-bottom: 15px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: 600; color: #555; }
        input[type="text"], input[type="password"], select { width: 100%; padding: 10px 12px; border-radius: 8px; border: 1px solid #d8d8d8; background-color: #fafafa; font-size: 1em; }
        input[type="submit"] { background-color: #ff9aa2; color: white; border: none; border-radius: 8px; padding: 12px; font-weight: bold; cursor: pointer; transition: background-color 0.3s ease; }
        input[type="submit"]:hover { background-color: #ff8e88; }
        .back-link { text-align: center; margin-top: 25px; }
        .back-link a { color: #ff8e88; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="container">
        <h1>📝 Đăng Ký Tài Khoản Mới</h1>

        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <form action="register" method="POST" autocomplete="off">
            <label for="ename">Họ và Tên:</label>
            <input type="text" id="ename" name="ename" required placeholder="Họ và Tên của bạn" autocomplete="off">

            <label for="username">Tên Đăng Nhập:</label>
            <input type="text" id="username" name="username" required placeholder="Dùng để đăng nhập" autocomplete="off">

            <label for="password">Mật Khẩu:</label>
            <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu" autocomplete="new-password">

            <label for="did">Phòng Ban Bạn Muốn Tham Gia:</label>
            <select id="did" name="did" required>
                <option value="" disabled selected>Chọn Phòng Ban</option>
                <c:forEach var="division" items="${requestScope.divisions}">
                    <option value="${division.id}">${division.name}</option>
                </c:forEach>
            </select>

            <input type="submit" value="ĐĂNG KÝ (Chờ Duyệt)">
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/index.html">← Quay về Trang Đăng Nhập</a>
        </div>
    </div>
</body>
</html>