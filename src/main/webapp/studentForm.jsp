<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 24/04/2025
  Time: 9:27 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản lý sinh viên</title>
    <meta charset="UTF-8">
    <style>
        .error { color: red; }
        .message { color: green; }
        label { display: inline-block; width: 150px; margin: 5px 0; }
        input, select { margin: 5px 0; }
    </style>
</head>
<body>
<h2>${student != null ? 'Chỉnh sửa sinh viên' : 'Thêm sinh viên mới'}</h2>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<c:if test="${not empty message}">
    <div class="message">${message}</div>
</c:if>

<form action="student" method="post">
    <c:if test="${student != null}">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="soCMND" value="${student.soCMND}"/>
    </c:if>

    <label for="soCMND">Số CMND:</label>
    <input type="text" id="soCMND" name="soCMND" value="${student != null ? student.soCMND : ''}" ${student != null ? 'readonly' : ''}/><br>

    <label for="hoTen">Họ Tên:</label>
    <input type="text" id="hoTen" name="hoTen" value="${student != null ? student.hoTen : ''}"/><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${student != null ? student.email : ''}"/><br>

    <label for="soDT">Số ĐT:</label>
    <input type="text" id="soDT" name="soDT" value="${student != null ? student.soDT : ''}"/><br>

    <label for="diaChi">Địa Chỉ:</label>
    <input type="text" id="diaChi" name="diaChi" value="${student != null ? student.diaChi : ''}"/><br>

    <label for="maTruong">Trường:</label>
    <select id="maTruong" name="maTruong">
        <c:forEach var="school" items="${schools}">
            <option value="${school[0]}" ${graduation != null && graduation.maTruong == school[0] ? 'selected' : ''}>${school[1]}</option>
        </c:forEach>
    </select><br>

    <label for="maNganh">Ngành:</label>
    <select id="maNganh" name="maNganh">
        <c:forEach var="major" items="${majors}">
            <option value="${major[0]}" ${graduation != null && graduation.maNganh == major[0] ? 'selected' : ''}>${major[1]}</option>
        </c:forEach>
    </select><br>

    <label for="heTN">Hệ TN:</label>
    <input type="text" id="heTN" name="heTN" value="${graduation != null ? graduation.heTN : ''}"/><br>

    <label for="ngayTN">Ngày TN:</label>
    <input type="date" id="ngayTN" name="ngayTN" value="${graduation != null ? graduation.ngayTN : ''}"/><br>

    <label for="loaiTN">Loại TN:</label>
    <input type="text" id="loaiTN" name="loaiTN" value="${graduation != null ? graduation.loaiTN : ''}"/><br>

    <input type="submit" value="${student != null ? 'Cập nhật' : 'Thêm sinh viên'}"/>
</form>

<br>
<a href="student?action=list">Danh sách sinh viên</a>
</body>
</html>
