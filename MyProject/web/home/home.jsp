<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
    Giả định: user.authorizedFeatures (Map<String, Boolean>) đã được nạp bởi BaseRequiredAuthorizationController.
--%>
<!DOCTYPE html>
<html>
    <head>
        <title>eLeave - Trang Chủ</title>
        <meta charset="UTF-8">
        <style>
            body { font-family: Arial, sans-serif; background-color: #f7f3e8; color: #3e3e3e; }
            .container { max-width: 900px; margin: 30px auto; }
            .header-info { border-bottom: 1px solid #a8c8c4; padding-bottom: 15px; margin-bottom: 20px; }
            .menu-bar { list-style: none; padding: 0; margin: 0; display: flex; background-color: #d8e8e8; border-radius: 8px; }
            .menu-bar li { padding: 10px 15px; }
            .menu-bar li a { text-decoration: none; color: #4b8a82; font-weight: bold; }
            .menu-bar li a:hover { color: #3e3e3e; }
            .user-details { text-align: right; margin-top: 10px; font-size: 0.9em; }
        </style>
    </head>
    <body>
        
        <c:set var="user" value="${sessionScope.auth}"/>
        
        <div class="container">

            <div class="user-details">
                Xin chào, **${user.displayname}** (${user.employee.dept.name}) | 
                <a href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
            </div>
            
            <div class="header-info">
                <h2>Bảng Điều Khiển Chính</h2>
            </div>
            
            <ul class="menu-bar">
                
                <li><a href="${pageContext.request.contextPath}/home">Trang Chủ</a></li>
                
                <c:if test="${user.authorizedFeatures['/request/create']}">
                    <li><a href="${pageContext.request.contextPath}/request/create">Tạo Đơn Nghỉ</a></li>
                </c:if>

                <c:if test="${user.authorizedFeatures['/request/list']}">
                    <li><a href="${pageContext.request.contextPath}/request/list">Quản Lý Đơn</a></li>
                </c:if>

                <c:if test="${user.authorizedFeatures['/division/agenda']}">
                    <li><a href="${pageContext.request.contextPath}/division/agenda">Tình Hình Lao Động</a></li>
                </c:if>
                
            </ul>
            
            <div style="margin-top: 30px;">
                <h1>Chào mừng bạn trở lại!</h1>
                <p>Bạn đã đăng nhập thành công. Các chức năng bạn có quyền sẽ hiển thị ở thanh menu trên.</p>
            </div>
        </div>
        
    </body>
</html>