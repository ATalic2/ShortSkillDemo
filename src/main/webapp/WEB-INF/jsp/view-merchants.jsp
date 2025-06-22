<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>View Merchants</title>
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
                    <th>Name</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${merchants}" var="merchant">
                    <tr>
                        <td><a href="/demo/merchant?id=${merchant.merchantId}">${merchant.merchantId}</a></td>
                        <td><a href="/demo/merchant?id=${merchant.merchantId}">${merchant.name}</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>