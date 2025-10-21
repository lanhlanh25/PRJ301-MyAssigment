<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Duyệt Đơn Xin Nghỉ Phép</title>
        <style>
            /* CSS của bạn */
            .review-form {
                border: 1px solid #ccc;
                padding: 20px;
                width: 400px;
            }
            .approved {
                color: green;
                font-weight: bold;
            }
            .rejected {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>

    <c:set var="request" value="${requestScope.request}"/>
    
    <h1>Duyệt đơn xin nghỉ phép</h1>

    <div class="review-form">

        <%-- I. HIỂN THỊ CHI TIẾT ĐƠN (Hình 6) --%>
        
        <p>Duyệt bởi User: ${sessionScope.auth.displayname}, Role: Quản lý</p>
        <p>Tạo bởi: ${request.createdBy.name}</p> 

        <p>Từ ngày: <fmt:formatDate value="${request.fromDate}" pattern="dd/MM/yyyy"/></p>
        <p>Tới ngày: <fmt:formatDate value="${request.toDate}" pattern="dd/MM/yyyy"/></p>
        
        <p>Lý do:</p>
        <div style="border: 1px solid #ddd; padding: 10px; background-color: #f9f9f9;">
            ${request.reason}
        </div>
        <br/>

        <%-- II. HIỂN THỊ TRẠNG THÁI VÀ HÀNH ĐỘNG --%>

        <p>Trạng thái: 
            <span class="${request.status eq 2 ? 'approved' : (request.status eq 3 ? 'rejected' : '')}">
                ${request.statusString}
            </span>
        </p>

        <%-- Form duyệt/từ chối (CHỈ HIỂN THỊ nếu canReview là TRUE) --%>
        <c:if test="${requestScope.canReview}">
            <h3>Hành động xét duyệt:</h3>
            <form action="review" method="POST">
                <input type="hidden" name="requestId" value="${request.id}"/>
                <p>Lý do Duyệt/Từ chối:</p>
                <textarea name="reasonForAction" rows="4" cols="50" placeholder="Bổ sung lý do duyệt hoặc không duyệt..."></textarea><br/><br/>

                <input type="submit" name="action" value="Reject" 
                       onclick="return confirm('Bạn có chắc chắn muốn TỪ CHỐI đơn này không?')" 
                       style="background-color: red; color: white; border: none; padding: 10px 20px; cursor: pointer;"/>

                <input type="submit" name="action" value="Approve" 
                       onclick="return confirm('Bạn có chắc chắn muốn DUYỆT đơn này không?')"
                       style="background-color: green; color: white; border: none; padding: 10px 20px; cursor: pointer;"/>
            </form>
        </c:if>

        <%-- III. HIỂN THỊ LỊCH SỬ XỬ LÝ (Nếu đơn đã được duyệt/từ chối) --%>
        <c:if test="${request.status ne 1}">
            <h3>Lịch sử xử lý:</h3>
            <p>Xử lý bởi: ${request.processedBy.name}</p>
            <p>Thời gian: <fmt:formatDate value="${request.processedTime}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
            <p>Lý do: ${request.reasonForAction}</p>
        </c:if>

    </div>
    </body>
</html>