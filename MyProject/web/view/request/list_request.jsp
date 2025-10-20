<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Danh Sách Đơn Xin Nghỉ Phép</title>
    </head>
    <body>
        <h1>Các Đơn Đã Tạo (Của Mình và Cấp Dưới)</h1>

        <a href="create">Tạo Đơn Mới</a><br/><br/>
        
        <table border="1">
            <tr>
                <th>Title</th>
                <th>From</th>
                <th>To</th>
                <th>Created By</th>
                <th>Status</th>
                <th>Processed By</th>
                <th>Action</th>
            </tr>
            <c:forEach var="request" items="${requestScope.requests}">
                <tr>
                    <td>${request.reason}</td> <%-- Dùng Reason làm Title --%>
                    <td>${request.fromDate}</td>
                    <td>${request.toDate}</td>
                    <td>${request.createdBy.name}</td>
                    <td>${request.statusString}</td>
                    <td><c:if test="${request.processedBy ne null}">${request.processedBy.name}</c:if></td>
                    <td>
                        <c:if test="${request.status eq 1}"> <%-- Chỉ hiện nút Duyệt nếu Status là In progress --%>
                            <a href="review?id=${request.id}">Review/View Detail</a>
                        </c:if>
                        <c:if test="${request.status ne 1}">
                            <a href="review?id=${request.id}">View Detail</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>