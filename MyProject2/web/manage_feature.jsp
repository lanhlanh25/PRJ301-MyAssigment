<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, model.Feature"%>
<%
ArrayList<Feature> features = (ArrayList<Feature>) request.getAttribute("features");
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Quản lý chức năng</title></head>
<body>
<h2>Danh sách Feature</h2>
<table border="1" cellpadding="5">
<tr><th>ID</th><th>URL</th><th>Mô tả</th></tr>
<% for(Feature f : features){ %>
<tr><td><%=f.getFeatureId()%></td><td><%=f.getUrl()%></td><td><%=f.getDescription()%></td></tr>
<% } %>
</table>
</body>
</html>
