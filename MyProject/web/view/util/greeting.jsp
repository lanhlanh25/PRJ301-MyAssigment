<%-- 
    Document   : greeting
    Created on : Oct 18, 2025, 11:10:55 AM
    Author     : sonnt
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${sessionScope.auth ne null}">
            Session of: ${sessionScope.auth.displayname}
            <br/>
            employee: ${sessionScope.auth.employee.id} - ${sessionScope.auth.employee.name}
        </c:if>
        <c:if test="${sessionScope.auth eq null}">
            You are not logged in yet!
        </c:if>    

        <%-- Bổ sung vào greeting.jsp --%>
        <c:if test="${sessionScope.auth ne null}">
            ...
            <br/>
            <p>
                [<a href="request/create">Tạo đơn nghỉ phép</a>]
                [<a href="request/list">Danh sách đơn</a>]

                <%-- Chỉ hiện Agenda nếu có quyền truy cập --%>
                <c:forEach var="role" items="${sessionScope.auth.roles}">
                    <c:forEach var="feature" items="${role.features}">
                        <c:if test="${feature.url eq '/division/agenda'}">
                            [<a href="division/agenda">Agenda</a>]
                        </c:if>
                    </c:forEach>
                </c:forEach>

                [<a href="logout">Đăng xuất</a>]
            </p>
        </c:if>
    </body>
</html>
