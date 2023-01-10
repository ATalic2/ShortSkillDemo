<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add merchant</title>
        <%@ include file="header.jsp" %>
    </head>
    <body>
        <c:if test="${addMerchantSuccess}">
            <div>Successfully added merchant with id: ${savedMerchant.merchantId}</div>
        </c:if>
    
        <c:url var="add_merchant_url" value="/merchant/addMerchant"/>
        <form:form action="${add_merchant_url}" method="post" modelAttribute="merchant">
            <form:label path="name">Name: </form:label> <form:input type="text" path="name" />
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>