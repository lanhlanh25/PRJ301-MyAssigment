<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login - eLeave System</title>
    <style>
        body {font-family: Segoe UI; background-color: #f4f9f9; display: flex; justify-content: center; align-items: center; height: 100vh;}
        .login-box {background: white; padding: 40px; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.1);}
        h2 {color: #009688;}
        input[type=text], input[type=password] {
            width: 100%; padding: 12px; margin: 10px 0; border: 1px solid #ddd; border-radius: 8px;
        }
        input[type=submit] {
            background-color: #009688; color: white; padding: 12px; border: none; border-radius: 8px; cursor: pointer;
            width: 100%; font-weight: bold;
        }
        input[type=submit]:hover {background-color: #00796B;}
        .error {color: red; margin-top: 10px;}
    </style>
</head>
<body>
    <div class="login-box">
        <h2>Admin Login</h2>
        <form method="POST" action="login">
            <input type="text" name="username" placeholder="Tên đăng nhập" required>
            <input type="password" name="password" placeholder="Mật khẩu" required>
            <input type="submit" value="Đăng nhập">
        </form>
        <div class="error">${requestScope.error}</div>
    </div>
</body>
</html>
