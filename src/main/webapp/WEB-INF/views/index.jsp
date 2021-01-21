<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main page</title>
</head>
<body>
    <h1>Hello World! Welcome to the main page!</h1>
    <a href="${pageContext.request.contextPath}/drivers">Check drivers</a>
    <a href="${pageContext.request.contextPath}/cars">Check cars</a>
    <a href="${pageContext.request.contextPath}/manufacturers">Check manufacturers</a>
    <a href="${pageContext.request.contextPath}/cars/drivers/add">Add driver to car</a>
</body>
</html>
