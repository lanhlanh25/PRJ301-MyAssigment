<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Danh Sách Đơn Xin Nghỉ Phép</title>
    <meta charset="UTF-8">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(120deg, #fef6f0, #f4f1ff);
            color: #444;
            min-height: 100vh;
        }

        /* Header */
        .header {
            width: 100%;
            background-color: #c3a6ff;
            color: #fff;
            padding: 12px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        .header h2 {
            font-size: 1.3em;
            letter-spacing: 0.5px;
        }

        .user-info {
            font-size: 0.95em;
        }

        .user-info a {
            color: #fff;
            text-decoration: underline;
            margin-left: 10px;
            font-weight: 600;
        }

        .container {
            width: 90%;
            max-width: 1000px;
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.08);
            padding: 40px 45px 50px;
            margin: 40px auto;
            animation: fadeIn 0.6s ease-in-out;
        }

        h1 {
            text-align: center;
            color: #a682ff;
            margin-bottom: 30px;
        }

        a.create-btn {
            display: inline-block;
            margin-bottom: 20px;
            background-color: #c3a6ff;
            color: white;
            text-decoration: none;
            font-weight: 600;
            padding: 10px 18px;
            border-radius: 8px;
            transition: background-color 0.3s ease, transform 0.1s ease;
        }

        a.create-btn:hover {
            background-color: #a682ff;
            transform: translateY(-1px);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
        }

        th, td {
            text-align: left;
            padding: 12px 15px;
        }

        th {
            background-color: #f1ecff;
            color: #555;
            font-weight: bold;
            border-bottom: 2px solid #d9caff;
        }

        tr:nth-child(even) {
            background-color: #fafafa;
        }

        tr:hover {
            background-color: #f9f4ff;
        }

        td a {
            color: #6b4eff;
            text-decoration: none;
            font-weight: 600;
        }

        td a:hover {
            text-decoration: underline;
        }

        .status {
            font-weight: 600;
            padding: 5px 10px;
            border-radius: 6px;
            display: inline-block;
        }

        .status.pending {
            background-color: #fff6d9;
            color: #b57d00;
        }

        .status.approved {
            background-color: #e8fff3;
            color: #2d8a4d;
        }

        .status.rejected {
            background-color: #ffeaea;
            color: #c0392b;
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

    <!-- Header trên cùng -->
    <div class="header">
        <h2>🌿 eLeave System</h2>
        <div class="user-info">
            Xin chào, <b>${sessionScope.auth.displayname}</b> |
            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </div>
    </div>

    <!-- Nội dung chính -->
    <div class="container">
        <h1>📄 Danh Sách Đơn Xin Nghỉ Phép</h1>

        <a href="create" class="create-btn">+ Tạo Đơn Mới</a>

        <table>
            <tr>
                <th>Tiêu đề</th>
                <th>Từ ngày</th>
                <th>Đến ngày</th>
                <th>Người tạo</th>
                <th>Trạng thái</th>
                <th>Người xử lý</th>
                <th>Hành động</th>
            </tr>

            <c:forEach var="request" items="${requestScope.requests}">
                <tr>
                    <td>${request.reason}</td>
                    <td>${request.fromDate}</td>
                    <td>${request.toDate}</td>
                    <td>${request.createdBy.name}</td>
                    <td>
                        <span class="status 
                            ${request.status eq 1 ? 'pending' : 
                              (request.status eq 2 ? 'approved' : 'rejected')}">
                            <c:choose>
                                <c:when test="${request.status eq 1}">🕓 ${request.statusString}</c:when>
                                <c:when test="${request.status eq 2}">✅ ${request.statusString}</c:when>
                                <c:otherwise>❌ ${request.statusString}</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>
                        <c:if test="${request.processedBy ne null}">
                            ${request.processedBy.name}
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${request.status eq 1}">
                                <a href="review?id=${request.id}">🔍 Duyệt / Xem</a>
                            </c:when>
                            <c:otherwise>
                                <a href="review?id=${request.id}">👁️ Xem chi tiết</a>
                            </c:otherwise>
                        </c:choose>
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
