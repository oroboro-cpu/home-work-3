<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Display cars</title>
</head>
<body>
    <h1>Cars displaying page!</h1>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Model</th>
            <th>Manufacturer</th>
            <th>Drivers</th>
        </tr>
        <c:forEach var="car" items="${cars}">
            <tr>
                <td>
                    <c:out value="${car.id}"/>
                </td>
                <td>
                    <c:out value="${car.name}"/>
                </td>
                <td>
                    <c:out value="${car.manufacturer}"/>
                </td>
                <td>
                    <c:out value="${car.drivers}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/cars/delete?id=${car.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="${pageContext.request.contextPath}/cars/create">Create cars</a>
</body>
</html>
