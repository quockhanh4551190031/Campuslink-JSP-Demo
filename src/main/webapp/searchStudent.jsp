<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 24/04/2025
  Time: 10:25 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Tìm kiếm sinh viên</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .error { color: red; }
        .message { color: green; }
    </style>
</head>
<body>
<h2>Tìm kiếm thông tin cơ bản của sinh viên</h2>
<form action="student" method="get">
    <input type="hidden" name="action" value="searchBasic">
    <div class="form-group">
        <label for="hoTen">Họ Tên:</label>
        <input type="text" id="hoTen" name="hoTen">
    </div>
    <div class="form-group">
        <input type="submit" value="Tìm kiếm">
    </div>
    <div>
        <a href="student?action=list">Quay lại danh sách</a>
    </div>
</form>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<c:if test="${students != null}">
    <h3>Kết quả tìm kiếm</h3>
    <c:if test="${empty students}">
        <p>Không tìm thấy sinh viên nào.</p>
    </c:if>
    <c:if test="${not empty students}">
        <table>
            <tr>
                <th>Số CMND</th>
                <th>Họ Tên</th>
                <th>Email</th>
                <th>Số ĐT</th>
                <th>Địa Chỉ</th>
            </tr>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student[0]}</td>
                    <td>${student[1]}</td>
                    <td>${student[2]}</td>
                    <td>${student[3]}</td>
                    <td>${student[4]}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</c:if>
</body>
</html>
