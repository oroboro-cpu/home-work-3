<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create drivers</title>
</head>
<body>
    <h1>Drivers creating page</h1>
    <form method="post" action="${pageContext.request.contextPath}/driver/create">
        Please, enter name for driver: <input type="text" name="name">
        Please, enter license number for driver: <input type="text" name="licenseNumber">
        <button type="submit">
            Create driver
        </button>
    </form>
</body>
</html>
