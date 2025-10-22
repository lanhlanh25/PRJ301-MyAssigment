<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Calendar, java.sql.Date, java.util.List, java.util.ArrayList, model.RequestForLeave, model.Employee"%>fđ
<!DOCTYPE html>
<html>
    <head>
        <title>Agenda Lao Động</title>
        <style>
            .present { background-color: lightgreen; } /* Bôi xanh là có đi làm */
            .leave { background-color: lightcoral; } /* Bôi đỏ là nghỉ phép */
            th { text-align: center; }
        </style>
    </head>
    <body>
        <h1>Agenda Tình Hình Lao Động Phòng ${sessionScope.auth.employee.dept.name}</h1>
        <p>Từ ngày ${requestScope.from} đến ${requestScope.to}</p>
        
        <table border="1" cellspacing="0" cellpadding="5">
            <tr>
                <th>Nhân sự</th>
                <%
                    // LOGIC TẠO DANH SÁCH NGÀY THÁNG
                    Date fromDate = (Date) request.getAttribute("from");
                    Date toDate = (Date) request.getAttribute("to");
                    
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(fromDate);
                    
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(toDate);
                    endCal.add(Calendar.DAY_OF_YEAR, 1); 
                    
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
                            // LOGIC KIỂM TRA NGÀY NGHỈ
                            Date currentDate = (Date) pageContext.getAttribute("date");
                            Employee emp = (Employee) pageContext.getAttribute("employee");
                            
                            // Lấy danh sách đơn Approved từ Controller
                            List<RequestForLeave> approvedLeaves = (List<RequestForLeave>) request.getAttribute("approvedLeaves");
                            
                            boolean isLeave = false;
                            
                            if (approvedLeaves != null) {
                                for (RequestForLeave leave : approvedLeaves) {
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
                            // Set Class cho Tag JSP
                            String cssClass = isLeave ? "leave" : "present";
                            pageContext.setAttribute("cssClass", cssClass);
                        %>
                        <td class="${cssClass}"></td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        
        <p style="margin-top: 20px;">
            <span style="background-color: lightgreen; padding: 2px 10px;"></span>: Đi làm (Bôi xanh) | 
            <span style="background-color: lightcoral; padding: 2px 10px;"></span>: Nghỉ phép (Bôi đỏ)
        </p>
    </body>
</html>