<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo Đơn Xin Nghỉ Phép</title>
    </head>
    <body>
        <h1>Đơn xin nghỉ phép</h1>

        <%-- Lấy thông tin User đã đăng nhập --%>
        <% 
            model.iam.User user = (model.iam.User)session.getAttribute("auth");
            String roleName = user.getRoles().isEmpty() ? "N/A" : user.getRoles().get(0).getName();
        %>

        

        <p>User: <%= user.getDisplayname() %>, Role: <%= roleName %>, Dep: <%= ((model.Division)user.getEmployee().getDept()).getName() %></p>
        <form action="create" method="POST">
            Từ ngày: <input type="date" name="fromDate" required><br/><br/>
            Tới ngày: <input type="date" name="toDate" required><br/><br/>
            Lý do:<br/>
            <textarea name="reason" rows="4" cols="50" required></textarea><br/><br/>
            <input type="submit" value="Gửi"/>
        </form>
    </body>
</html>