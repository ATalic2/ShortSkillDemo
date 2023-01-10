<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Merchant</title>
        <%@ include file="header.jsp" %>
    </head>
    
    <body>
    	<c:if test="${updateMerchantSuccess}">
            <div>Successfully updated merchant with id: ${updatedMerchantId}</div>
        </c:if>
        
    	<c:url var="update_merchant_url" value="/merchant?id=${merchant.merchantId}"/>
        <form:form action="${update_merchant_url}" method="post" modelAttribute="merchant">
            <form:label path="name">Name: </form:label> <form:input type="text" path="name" />
            <input type="submit" value="Update"/>
        </form:form>
    </body>
</html>