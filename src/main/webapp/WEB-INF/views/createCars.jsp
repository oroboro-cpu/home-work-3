<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create cars</title>
</head>
<body>
    <h1>Cars creating page!</h1>
    <form method="post" action="${pageContext.request.contextPath}/cars/create">
        Please, enter model for car: <input type="text" name="name">
        Please, enter ID for manufacturer: <input type="text" name="manufacturerId">
        <button type="submit">
            Create car!
        </button>
    </form>
</body>
</html>
