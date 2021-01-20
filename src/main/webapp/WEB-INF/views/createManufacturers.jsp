<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create manufacturer</title>
</head>
<body>
    <h1>Manufacturer creating page!</h1>
    <form method="post" action="${pageContext.request.contextPath}/manufacturers/create">
        Please, enter name for manufacturer: <input type="text" name="name">
        Please, enter country for manufacturer: <input type="text" name="country">
        <button type="submit">
            Create manufacturer
        </button>
    </form>
</body>
</html>
