<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add client</title>
        <%@ include file="header.jsp" %>
    </head>
    <body>
        <c:if test="${addClientSuccess}">
            <div>Successfully added client with id: ${savedClientId}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>
    
        <c:url var="add_client_url" value="/client/addClient"/>
        <form:form action="${add_client_url}" method="post" modelAttribute="client">
            <form:label path="firstName">First Name: </form:label> <form:input type="text" path="firstName"/>
            <form:label path="lastName">Last Name: </form:label> <form:input type="text" path="lastName"/>
            <form:label path="job">Job: </form:label> <form:input path="job"/>
            <form:select path="merchantId">
            	<c:forEach items="${merchants}" var="merchant">
					<form:option value="${merchant.merchantId}" label="${merchant.name}"/>
				</c:forEach>
			</form:select>  
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>