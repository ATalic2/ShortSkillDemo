<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="<c:url value='/login'/>" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required /><br/><br/>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required /><br/><br/>
        <button type="submit">Login</button>
    </form>
    <c:if test="${not empty param.error}">
        <div style="color:red;">Invalid username or password.</div>
    </c:if>
    <c:if test="${not empty param.logout}">
        <div style="color:green;">You have been logged out.</div>
    </c:if>
</body>
</html>