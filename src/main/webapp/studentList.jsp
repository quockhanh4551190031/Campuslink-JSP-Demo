<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách sinh viên</title>
    <meta charset="UTF-8">
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .error { color: red; }
        .message { color: green; }
    </style>
</head>
<body>
<h2>Danh sách sinh viên</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<c:if test="${not empty message}">
    <div class="message">${message}</div>
</c:if>

<a href="student">Thêm sinh viên mới</a> |
<a href="student?action=searchBasic">Tìm kiếm thông tin cơ bản</a> |
<a href="student?action=searchGradJob">Tìm kiếm thông tin tốt nghiệp và việc làm</a>
<br><br>

<c:if test="${empty students}">
    <p>Không có sinh viên nào trong danh sách.</p>
</c:if>

<c:if test="${not empty students}">
    <table>
        <tr>
            <th>Số CMND</th>
            <th>Họ Tên</th>
            <th>Email</th>
            <th>Số ĐT</th>
            <th>Địa Chỉ</th>
            <th>Trường</th>
            <th>Ngành</th>
            <th>Hệ TN</th>
            <th>Ngày TN</th>
            <th>Loại TN</th>
            <th>Thao tác</th>
        </tr>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student[0]}</td>
                <td>${student[1]}</td>
                <td>${student[2]}</td>
                <td>${student[3]}</td>
                <td>${student[4]}</td>
                <td>${student[6]}</td>
                <td>${student[8]}</td>
                <td>${student[9]}</td>
                <td>${student[10]}</td>
                <td>${student[11]}</td>
                <td>
                    <a href="student?action=edit&soCMND=${student[0]}">Sửa</a> |
                    <a href="student?action=delete&soCMND=${student[0]}" onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên này?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty students}">
    <p>Không có sinh viên nào trong danh sách.</p>
</c:if>
</body>
</html>