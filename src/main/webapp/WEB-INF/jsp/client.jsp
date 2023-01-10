<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<script type="text/javascript" src="js/deleteClient.js"></script>
    <head>
        <title>Client</title>
        <%@ include file="header.jsp" %>
    </head>
    
    <body>
    	<c:if test="${updateClientSuccess}">
            <div>Successfully updated client with id: ${updatedClientId}</div>
        </c:if>
        
    	<c:url var="update_client_url" value="/client?id=${client.id}"/>
        <form:form action="${update_client_url}" method="post" modelAttribute="client">
            <form:label path="firstName">First Name: </form:label> <form:input type="text" path="firstName" />
            <form:label path="lastName" >Last Name: </form:label> <form:input type="text" path="lastName"/>
            <form:label path="job" >Job: </form:label> <form:input path="job"/>
            <form:select path="merchantId">
            	<c:forEach items="${merchants}" var="merchant">
					<form:option value="${merchant.merchantId}" label="${merchant.name}"/>
				</c:forEach>
			</form:select>  
            <input type="submit" value="Update"/>
        	<button type="button" onclick="javascript:deleteClient(${client.id});">Delete</button>
        </form:form>
    </body>
</html>