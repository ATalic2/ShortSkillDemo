<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<script type="text/javascript" src="../js/deleteClient.js"></script>
    <head>
        <title>View Clients</title>
        <%@ include file="header.jsp" %>
    </head>
    
    <body>
        <c:if test="${deleteClientSuccess}">
            <div class="success">Client deleted successfully!</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>
        <table>
            <thead>
                <tr>
                    <th>id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Job</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${clients}" var="client">
                    <tr>
                        <td><a href="/demo/client?id=${client.id}">${client.id}</a></td>
                        <td><a href="/demo/client?id=${client.id}">${client.firstName}</a></td>
                        <td><a href="/demo/client?id=${client.id}">${client.lastName}</a></td>
                        <td>${client.job}</td>
                        <td><button type="button" onclick="javascript:deleteClient(${client.id});">Delete</button></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>