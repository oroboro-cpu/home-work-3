<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add driver to car</title>
</head>
<body>
    <h1>Adding driver to car page</h1>
    <form method="post" action="${pageContext.request.contextPath}/cars/drivers/add">
        Please, enter id for car: <input type="text" name="carId">
        Please, enter id for driver: <input type="text" name="driverId">
        <button type="submit">
            Add driver
        </button>
    </form>
</body>
</html>
