<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Nhân Viên và Chức Vụ</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(120deg, #fef6f0, #f4f1ff); color: #444; min-height: 100vh; padding: 40px 20px; }
        .container { width: 90%; max-width: 1000px; margin: auto; background-color: #fff; border-radius: 16px; box-shadow: 0 6px 20px rgba(0,0,0,0.08); padding: 40px 45px 50px; }
        h1 { text-align: center; color: #a682ff; margin-bottom: 30px; }
        .message { padding: 10px; margin-bottom: 20px; border-radius: 8px; text-align: center; font-weight: bold; }
        .message.success { background-color: #e8fff3; color: #2d8a4d; border: 1px solid #b8eac6; }
        .message.error { background-color: #ffeaea; color: #c0392b; border: 1px solid #f5b7b1; }
        table { width: 100%; border-collapse: collapse; border-radius: 12px; overflow: hidden; box-shadow: 0 3px 10px rgba(0,0,0,0.05); }
        th, td { text-align: left; padding: 12px 15px; border-bottom: 1px solid #eee; }
        th { background-color: #f1ecff; color: #555; font-weight: bold; }
        tr:nth-child(even) { background-color: #fafafa; }
        .back-link { text-align: center; margin-top: 25px; }
        .back-link a { color: #a682ff; font-weight: bold; text-decoration: none; }
        .action-form { display: flex; align-items: center; gap: 8px; }
        select { padding: 6px; border-radius: 6px; border: 1px solid #ccc; }
        input[type="submit"] { padding: 6px 12px; background-color: #c3a6ff; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: 600; }
        input[type="submit"]:hover { background-color: #a682ff; }
        .btn-deactivate { background-color: #ff5757; }
        .btn-deactivate:hover { background-color: #e04a4a; }
        .btn-activate { background-color: #4CAF50; }
        .btn-activate:hover { background-color: #45a049; }
    </style>
</head>
<body>

    <div class="container">
        <h1>👥 Quản Lý Nhân Viên</h1>

        <c:if test="${not empty message}">
            <div class="message success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <table>
            <tr>
                <th>ID</th>
                <th>Tên nhân viên</th>
                <th>Phòng ban</th>
                <th>Tên đăng nhập (UID)</th>
                <th>Chức vụ hiện tại</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            <c:forEach var="emp" items="${requestScope.employees}">
                <tr>
                    <td>${emp.id}</td>
                    <td>${emp.name}</td>
                    <td>${emp.dept.name}</td>
                    <td>
                        <c:if test="${emp.user ne null}">
                            ${emp.user.username} (ID: ${emp.user.id})
                        </c:if>
                    </td>
                    <td>
                        <c:set var="currentRole" value="N/A"/>
                        <c:if test="${emp.user ne null}">
                            <c:forEach var="role" items="${emp.user.roles}">
                                <c:set var="currentRole" value="${role.name}"/>
                            </c:forEach>
                        </c:if>
                        <b>${currentRole}</b>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${emp.active}">
                                <span style="color: green; font-weight: bold;">✅ Active</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: red; font-weight: bold;">❌ Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="manage" method="POST" class="action-form">
                            <input type="hidden" name="uid" value="${emp.user.id}"/>
                            
                            <select name="newRoleId" required>
                                <option value="" disabled selected>Chọn chức vụ mới</option>
                                <c:forEach var="role" items="${requestScope.roles}">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" name="action" value="promote"
                                   onclick="return confirm('Bạn có chắc chắn muốn thay đổi chức vụ của ${emp.name} không?')"
                                   value="Thăng chức"/>
                        </form>
                        
                        <form action="manage" method="POST" style="margin-top: 5px;">
                            <input type="hidden" name="uid" value="${emp.user.id}"/>
                            <c:if test="${emp.active}">
                                <input type="submit" name="action" 
                                       class="btn-deactivate"
                                       onclick="return confirm('Xác nhận SA THẢI và vô hiệu hóa tài khoản ${emp.name}?')"
                                       value="deactivate"/>
                            </c:if>
                            <c:if test="${!emp.active}">
                                <input type="submit" name="action" 
                                       class="btn-activate"
                                       onclick="return confirm('Xác nhận KÍCH HOẠT lại tài khoản ${emp.name}?')"
                                       value="activate"/>
                            </c:if>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/home">← Quay về Trang Chủ</a>
        </div>
    </div>

</body>
</html>