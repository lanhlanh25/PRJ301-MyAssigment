<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Calendar, java.sql.Date, java.util.List, java.util.ArrayList, model.RequestForLeave, model.Employee"%>
<!DOCTYPE html>
<html>
<head>
    <title>Agenda Lao Động</title>
    <meta charset="UTF-8">
    <style>
        /* --- Reset --- */
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
            flex-direction: column;
            align-items: center;
            padding: 40px 20px;
        }

        .container {
            width: 95%;
            max-width: 1200px;
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.08);
            padding: 40px 45px 50px;
            animation: fadeIn 0.6s ease-in-out;
        }

        h1 {
            text-align: center;
            color: #a682ff;
            margin-bottom: 10px;
        }

        p.sub {
            text-align: center;
            color: #666;
            font-size: 0.95em;
            margin-bottom: 25px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 3px 10px rgba(0,0,0,0.05);
        }

        th, td {
            text-align: center;
            padding: 10px 8px;
            border-bottom: 1px solid #eee;
            font-size: 0.95em;
        }

        th {
            background-color: #ede7ff;
            color: #4a3e77;
            font-weight: bold;
        }

        tr:nth-child(even) {
            background-color: #fafafa;
        }

        tr:hover {
            background-color: #f9f4ff;
        }

        .present {
            background-color: #d8fdd8 !important; /* xanh nhạt */
        }

        .leave {
            background-color: #ffd6d6 !important; /* đỏ nhạt */
        }

        .legend {
            margin-top: 20px;
            font-size: 0.95em;
            text-align: center;
        }

        .legend span {
            display: inline-block;
            padding: 4px 14px;
            border-radius: 8px;
            margin: 0 5px;
        }

        .legend .present-sample {
            background-color: #d8fdd8;
            border: 1px solid #b0e2b0;
        }

        .legend .leave-sample {
            background-color: #ffd6d6;
            border: 1px solid #f5a7a7;
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

    <div class="container">
        <h1>📊 Agenda Tình Hình Lao Động</h1>
        <p class="sub">
            Phòng: <b>${sessionScope.auth.employee.dept.name}</b> <br>
            Từ ngày <b>${requestScope.from}</b> đến <b>${requestScope.to}</b>
        </p>

        <table>
            <tr>
                <th>Nhân sự</th>
                <%
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
                    <td style="font-weight: 600;">${employee.name}</td>
                    <c:forEach var="date" items="${dates}">
                        <%
                            Date currentDate = (Date) pageContext.getAttribute("date");
                            Employee emp = (Employee) pageContext.getAttribute("employee");
                            List<RequestForLeave> approvedLeaves = (List<RequestForLeave>) request.getAttribute("approvedLeaves");
                            boolean isLeave = false;
                            if (approvedLeaves != null) {
                                for (RequestForLeave leave : approvedLeaves) {
                                    if (leave.getCreatedBy().getId() == emp.getId()) {
                                        if (currentDate.compareTo(leave.getFromDate()) >= 0 &&
                                            currentDate.compareTo(leave.getToDate()) <= 0) {
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

        <div class="legend">
            <span class="present-sample"></span> Đi làm &nbsp;&nbsp;|&nbsp;&nbsp;
            <span class="leave-sample"></span> Nghỉ phép
        </div>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/home">← Quay về Trang Chủ</a>
        </div>
    </div>

</body>
</html>
