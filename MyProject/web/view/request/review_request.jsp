<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Duyệt Đơn Xin Nghỉ Phép</title>
    <meta charset="UTF-8">
    <style>
        /* --- Reset mặc định --- */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(120deg, #fdf6f0, #f2f4ff);
            color: #444;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 40px 0;
        }

        .review-container {
            width: 600px;
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.08);
            padding: 40px 45px 50px;
            animation: fadeIn 0.6s ease-in-out;
        }

        h1 {
            text-align: center;
            color: #a682ff;
            margin-bottom: 30px;
        }

        h3 {
            color: #555;
            margin-top: 25px;
            margin-bottom: 10px;
        }

        p {
            line-height: 1.6;
            margin-bottom: 8px;
        }

        .meta-info {
            background-color: #fafaff;
            padding: 12px 16px;
            border-left: 4px solid #a682ff;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.95em;
        }

        .info-box {
            background-color: #f9f9fb;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #eee;
            margin-bottom: 15px;
        }

        .status {
            font-weight: bold;
            font-size: 1.05em;
            padding: 6px 12px;
            border-radius: 6px;
            display: inline-block;
        }

        .approved {
            background-color: #e8fff3;
            color: #2d8a4d;
            border: 1px solid #b8eac6;
        }

        .rejected {
            background-color: #ffeaea;
            color: #c0392b;
            border: 1px solid #f5b7b1;
        }

        .pending {
            background-color: #fffaf0;
            color: #b57d00;
            border: 1px solid #f8d78d;
        }

        form {
            margin-top: 15px;
        }

        textarea {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #d8d8d8;
            resize: vertical;
            font-size: 0.95em;
            background-color: #fafafa;
            transition: 0.3s;
        }

        textarea:focus {
            outline: none;
            border-color: #a682ff;
            box-shadow: 0 0 6px rgba(166,130,255,0.4);
        }

        .btn {
            padding: 10px 22px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.2s ease;
            margin-right: 10px;
        }

        .btn-approve {
            background-color: #a5e8ba;
            color: #2e7d32;
        }

        .btn-approve:hover {
            background-color: #81d69b;
        }

        .btn-reject {
            background-color: #ffc1c1;
            color: #a93226;
        }

        .btn-reject:hover {
            background-color: #ffaaaa;
        }

        .back-link {
            text-align: center;
            margin-top: 25px;
        }

        .back-link a {
            color: #a682ff;
            font-weight: bold;
            text-decoration: none;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>

    <div class="review-container">
        <h1>📋 Duyệt Đơn Xin Nghỉ Phép</h1>

        <div class="meta-info">
            Duyệt bởi: <b>${sessionScope.auth.displayname}</b> (Quản lý)
        </div>

        <div class="info-box">
            <p><b>Tạo bởi:</b> ${requestScope.request.createdBy.name}</p>
            <p><b>Từ ngày:</b> <fmt:formatDate value="${requestScope.request.fromDate}" pattern="dd/MM/yyyy"/></p>
            <p><b>Tới ngày:</b> <fmt:formatDate value="${requestScope.request.toDate}" pattern="dd/MM/yyyy"/></p>
            <p><b>Lý do:</b></p>
            <div style="background-color:#fffdfd; border:1px solid #eee; padding:10px; border-radius:8px;">
                ${requestScope.request.reason}
            </div>
        </div>

        <p><b>Trạng thái:</b>
            <span class="status 
                ${requestScope.request.status eq 2 ? 'approved' :
                  (requestScope.request.status eq 3 ? 'rejected' : 'pending')}">
                ${requestScope.request.statusString}
            </span>
        </p>

        <c:if test="${requestScope.canReview}">
            <h3>Hành động xét duyệt:</h3>
            <form action="review" method="POST">
                <input type="hidden" name="requestId" value="${requestScope.request.id}"/>
                <textarea name="reasonForAction" rows="4" placeholder="Nhập lý do duyệt hoặc từ chối..."></textarea><br/><br/>

                <input type="submit" name="action" value="Reject"
                       class="btn btn-reject"
                       onclick="return confirm('Bạn có chắc chắn muốn TỪ CHỐI đơn này không?')"/>

                <input type="submit" name="action" value="Approve"
                       class="btn btn-approve"
                       onclick="return confirm('Bạn có chắc chắn muốn DUYỆT đơn này không?')"/>
            </form>
        </c:if>

        <c:if test="${requestScope.request.status ne 1}">
            <h3>Lịch sử xử lý:</h3>
            <div class="info-box">
                <p><b>Xử lý bởi:</b> ${requestScope.request.processedBy.name}</p>
                <p><b>Thời gian:</b> <fmt:formatDate value="${requestScope.request.processedTime}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                <p><b>Lý do:</b> ${requestScope.request.reasonForAction}</p>
            </div>
        </c:if>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/request/list">← Quay về danh sách đơn</a>
        </div>
    </div>

</body>
</html>
