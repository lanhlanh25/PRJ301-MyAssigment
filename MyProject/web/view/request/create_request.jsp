<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tạo Đơn Xin Nghỉ Phép</title>
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
            background: linear-gradient(120deg, #fef6f0, #f4f1ff);
            color: #444;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            width: 500px;
            padding: 40px 45px;
            animation: fadeIn 0.5s ease-in-out;
        }

        h1 {
            text-align: center;
            color: #a682ff;
            margin-bottom: 25px;
        }

        p {
            text-align: center;
            margin-bottom: 25px;
            font-size: 0.95em;
            color: #666;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        label {
            font-weight: 600;
            color: #555;
        }

        input[type="date"],
        textarea {
            width: 100%;
            padding: 10px 12px;
            border-radius: 8px;
            border: 1px solid #d8d8d8;
            background-color: #fafafa;
            font-size: 1em;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        input[type="date"]:focus,
        textarea:focus {
            outline: none;
            border-color: #c3a6ff;
            box-shadow: 0 0 6px rgba(195, 166, 255, 0.4);
        }

        textarea {
            resize: vertical;
        }

        input[type="submit"] {
            background-color: #c3a6ff;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.1s ease;
        }

        input[type="submit"]:hover {
            background-color: #a682ff;
            transform: translateY(-1px);
        }

        .back-link {
            text-align: center;
            margin-top: 15px;
        }

        .back-link a {
            color: #a682ff;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(15px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🌸 Đơn Xin Nghỉ Phép</h1>

        <% 
            model.iam.User user = (model.iam.User)session.getAttribute("auth");
            String roleName = user.getRoles().isEmpty() ? "N/A" : user.getRoles().get(0).getName();
        %>

        <p>
            👤 <b><%= user.getDisplayname() %></b> &nbsp;|&nbsp;
            🏢 <%= ((model.Division)user.getEmployee().getDept()).getName() %> &nbsp;|&nbsp;
            🎯 Vai trò: <%= roleName %>
        </p>

        <form action="create" method="POST">
            <label for="fromDate">Từ ngày:</label>
            <input type="date" id="fromDate" name="fromDate" required>

            <label for="toDate">Tới ngày:</label>
            <input type="date" id="toDate" name="toDate" required>

            <label for="reason">Lý do:</label>
            <textarea id="reason" name="reason" rows="4" required placeholder="Nhập lý do nghỉ phép..."></textarea>

            <input type="submit" value="📩 Gửi Đơn">
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/home">← Quay về Trang Chủ</a>
        </div>
    </div>
</body>
</html>
