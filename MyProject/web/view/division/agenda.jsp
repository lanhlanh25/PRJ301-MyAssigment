<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Calendar, java.sql.Date, java.util.concurrent.TimeUnit"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Agenda Lao Động</title>
        <style>
            .present { background-color: lightgreen; } /* Bôi xanh */
            .leave { background-color: lightcoral; } /* Bôi đỏ */
        </style>
    </head>
    <body>
        <h1>Agenda Tình Hình Lao Động</h1>
        <p>Từ ngày ${requestScope.from} đến ${requestScope.to}</p>
        
        <table border="1">
            <tr>
                <th>Nhân sự</th>
                <%
                    // Lấy khoảng ngày từ Controller
                    Date fromDate = (Date) request.getAttribute("from");
                    Date toDate = (Date) request.getAttribute("to");
                    
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(fromDate);
                    
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(toDate);
                    endCal.add(Calendar.DAY_OF_YEAR, 1); // Đảm bảo bao gồm cả ngày cuối
                    
                    // Tạo danh sách các ngày trong khoảng
                    List<Date> dates = new ArrayList<>();
                    while (startCal.before(endCal)) {
                        dates.add(new Date(startCal.getTimeInMillis()));
                        startCal.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    request.setAttribute("dates", dates);
                %>
                
                <c:forEach var="date" items="${dates}">
                    <th><fmt:formatDate value="${date}" pattern="d/M"/></th>
                </c:forEach>
            </tr>
            
            <c:forEach var="employee" items="${requestScope.employees}">
                <tr>
                    <td>${employee.name}</td>
                    <c:forEach var="date" items="${dates}">
                        <%
                            // Logic kiểm tra ngày nghỉ:
                            Date currentDate = (Date) pageContext.getAttribute("date");
                            model.Employee emp = (model.Employee) pageContext.getAttribute("employee");
                            List<model.RequestForLeave> approvedLeaves = (List<model.RequestForLeave>) request.getAttribute("approvedLeaves");
                            
                            boolean isLeave = false;
                            if (approvedLeaves != null) {
                                for (model.RequestForLeave leave : approvedLeaves) {
                                    // Kiểm tra xem Employee có phải là người nghỉ phép không
                                    if (leave.getCreatedBy().getId() == emp.getId()) {
                                        // Kiểm tra xem ngày hiện tại có nằm trong khoảng nghỉ không
                                        if (currentDate.compareTo(leave.getFromDate()) >= 0 && currentDate.compareTo(leave.getToDate()) <= 0) {
                                            isLeave = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            String cssClass = isLeave ? "leave" : "present";
                            pageContext.setAttribute("cssClass", cssClass);
                        %>
                        <td class="${cssClass}"></td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>