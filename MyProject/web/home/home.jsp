<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>eLeave - Trang Chủ</title>
    <meta charset="UTF-8">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(135deg, #e9f5f3, #fdfcfb);
            color: #333;
            min-height: 100vh;
        }

        .container {
            width: 90%;
            max-width: 1000px;
            margin: 40px auto;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            padding: 25px 40px 50px;
        }

        /* Account box */
        .account-box {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            background-color: #f5f3ff;
            padding: 10px 15px;
            border-radius: 12px;
            box-shadow: 0 3px 8px rgba(0,0,0,0.08);
            gap: 12px;
            margin-bottom: 15px;
        }
        .account-box .avatar img {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            border: 2px solid #a682ff;
            object-fit: cover;
        }
        .account-box .info {
            text-align: right;
            font-size: 0.9em;
        }
        .account-box .actions a {
            color: #6b4eff;
            text-decoration: none;
            font-weight: 600;
        }
        .account-box .actions a:hover {
            text-decoration: underline;
        }

        .header-info h2 {
            color: #2b7a78;
            font-size: 1.8em;
            text-align: center;
            border-bottom: 3px solid #2b7a78;
            padding-bottom: 10px;
            margin-bottom: 25px;
        }

        .menu-bar {
            list-style: none;
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 15px;
            background-color: #def2f1;
            border-radius: 10px;
            padding: 15px 0;
            margin-bottom: 30px;
        }
        .menu-bar li { transition: transform 0.2s ease; }
        .menu-bar li:hover { transform: translateY(-2px); }
        .menu-bar li a {
            text-decoration: none;
            color: #17252a;
            background-color: #3aafa9;
            padding: 10px 18px;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .menu-bar li a:hover {
            background-color: #2b7a78;
            color: #fff;
            box-shadow: 0 4px 8px rgba(43, 122, 120, 0.3);
        }

        .content { text-align: center; line-height: 1.6; }
        .content h1 { font-size: 2em; color: #3aafa9; margin-bottom: 10px; }

        footer {
            margin-top: 40px;
            text-align: center;
            color: #888;
            font-size: 0.9em;
        }
    </style>
</head>
<body>

    <c:set var="user" value="${sessionScope.auth}"/>

    <div class="container">

        <div class="account-box">
            <div class="avatar">
                <img src="${pageContext.request.contextPath}/images/user.png" alt="Avatar">
            </div>
            <div class="info">
                <strong>${user.displayname}</strong><br>
                <span>${user.username}</span>
            </div>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/profile">⚙️ Tài khoản</a> |
                <a href="${pageContext.request.contextPath}/logout">🚪 Đăng Xuất</a>
            </div>
        </div>

        <div class="header-info">
            <h2>🏢 Trang Chủ eLeave</h2>
        </div>

        <ul class="menu-bar">
            <li><a href="${pageContext.request.contextPath}/home">Trang Chủ</a></li>

            <c:if test="${user.authorizedFeatures['/request/create']}">
                <li><a href="${pageContext.request.contextPath}/request/create">📝 Tạo Đơn Nghỉ</a></li>
            </c:if>

            <c:if test="${user.authorizedFeatures['/request/list']}">
                <li><a href="${pageContext.request.contextPath}/request/list">📂 Quản Lý Đơn</a></li>
            </c:if>

            <c:if test="${user.authorizedFeatures['/division/agenda']}">
                <li><a href="${pageContext.request.contextPath}/division/agenda">📊 Tình Hình Lao Động</a></li>
            </c:if>

            <c:if test="${user.authorizedFeatures['/employee/manage']}">
                <li><a href="${pageContext.request.contextPath}/employee/manage">👔 Quản Lý Nhân Viên</a></li>
            </c:if>
            
        
        </ul>

        <div class="content">
            <h1>Chào mừng bạn trở lại, ${user.displayname}!</h1>
            <p>Bạn đã đăng nhập thành công. Các chức năng bạn có quyền sẽ hiển thị trên thanh menu phía trên.</p>
        </div>

        <footer>
            <p>© 2025 eLeave System — Managed by PRJ301 Team</p>
        </footer>
    </div>
</body>
</html>