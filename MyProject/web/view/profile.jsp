<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thông Tin Tài Khoản</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(120deg, #fef6f0, #f4f1ff);
            color: #444;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .profile-container {
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.08);
            width: 450px;
            padding: 40px;
            animation: fadeIn 0.6s ease-in-out;
        }
        h1 { text-align: center; color: #a682ff; margin-bottom: 25px; }
        label { font-weight: 600; color: #555; display: block; margin-bottom: 5px; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 10px; border-radius: 8px;
            border: 1px solid #ccc; background: #fafafa; margin-bottom: 15px;
        }
        input:focus { outline: none; border-color: #a682ff; box-shadow: 0 0 6px rgba(166,130,255,0.4); }
        .btn { width: 100%; padding: 12px; border: none; border-radius: 8px; font-weight: bold; cursor: pointer; }
        .btn-save { background-color: #c3a6ff; color: white; }
        .btn-save:hover { background-color: #a682ff; }
        .btn-cancel { background-color: #ddd; color: #444; margin-top: 10px; }
        .btn-cancel:hover { background-color: #ccc; }
        .message { text-align: center; font-weight: bold; margin-bottom: 15px; }
        .success { color: green; }
        .error { color: red; }
    </style>
</head>
<body>
    <div class="profile-container">
        <h1>👤 Thông Tin Tài Khoản</h1>

        <c:if test="${not empty message}">
            <div class="message ${messageType}">${message}</div>
        </c:if>

        <form action="profile" method="POST">
            <label>Tên hiển thị:</label>
            <input type="text" name="displayname" value="${sessionScope.auth.displayname}" required>

            <label>Tên đăng nhập:</label>
            <input type="text" name="username" value="${sessionScope.auth.username}" required>

            <label>Mật khẩu mới:</label>
            <input type="password" name="password" placeholder="Nhập mật khẩu mới (nếu muốn đổi)">

            <button type="submit" class="btn btn-save">💾 Lưu Thay Đổi</button>
            <a href="${pageContext.request.contextPath}/home">
                <button type="button" class="btn btn-cancel">↩ Quay về Trang chủ</button>
            </a>
        </form>
    </div>
</body>
</html>
