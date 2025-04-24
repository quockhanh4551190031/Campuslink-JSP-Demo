<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 24/04/2025
  Time: 10:29 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Tìm việc làm cho sinh viên</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .error { color: red; }
        .message { color: green; }
    </style>
</head>
<body>
<h2>Tìm kiếm thông tin tốt nghiệp và việc làm của sinh viên</h2>
<form action="student" method="get">
    <input type="hidden" name="action" value="searchGradJob">
    <div class="form-group">
        <label for="soCMND">Số CMND:</label>
        <input type="number" id="soCMND" name="soCMND" required min="1">
    </div>
    <div class="form-group">
        <input type="submit" value="Tìm kiếm">
    </div>
    <div>
        <a href="student?action=list"><button type="button">Quay lại danh sách</button></a>
    </div>
</form>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<c:if test="${results != null}">
    <h3>Kết quả tìm kiếm</h3>
    <c:if test="${empty results}">
        <p>Không tìm thấy thông tin tốt nghiệp hoặc việc làm nào.</p>
    </c:if>
    <c:if test="${not empty results}">
        <table>
            <tr>
                <th>Số CMND</th>
                <th>Họ Tên</th>
                <th>Mã Ngành (Tốt nghiệp)</th>
                <th>Mã Trường (Tốt nghiệp)</th>
                <th>Mã Ngành (Công ty)</th>
                <th>Tên Công ty</th>
                <th>Thời Gian Làm Việc</th>
            </tr>
            <c:forEach var="result" items="${results}">
                <tr>
                    <td>${result[0]}</td>
                    <td>${result[1]}</td>
                    <td>${result[2]}</td>
                    <td>${result[3]}</td>
                    <td>${result[4]}</td>
                    <td>${result[5]}</td>
                    <td>${result[6]}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</c:if>
</body>
</html>
