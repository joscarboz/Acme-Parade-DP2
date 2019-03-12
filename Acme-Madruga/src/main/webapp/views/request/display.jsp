<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<b><spring:message code="request.status" /></b>
<jstl:out value="${request.status}" />
<br />

<b><spring:message code="request.parade" /></b>
<jstl:out value="${request.parade.ticker}" />
<br />

<b><spring:message code="request.name" /></b>
<jstl:out value="${request.member.name}" />
<br />

<b><spring:message code="request.surname" /></b>
<jstl:out value="${request.member.surname}" />
<br />


<jstl:choose>
<jstl:when test="${request.status == 'REJECTED'}">
	<b><spring:message code="request.rejectionReason" /></b>
	<jstl:out value="${request.rejectionReason}" />
	<br />
</jstl:when>
<jstl:otherwise>
	<b><spring:message code="request.row" /></b>
	<jstl:out value="${request.row}" />
	<br />

	<b><spring:message code="request.column" /></b>
	<jstl:out value="${request.column}" />
	<br />
</jstl:otherwise>
</jstl:choose>

<jstl:choose>	
<jstl:when test="${request.status == 'PENDING'}">
	<security:authorize access="hasRole('MEMBER')">
		<a href="request/member/delete.do?requestId=${request.id}"><spring:message
				code="request.delete" /></a>
		<br />
	</security:authorize>
</jstl:when>
</jstl:choose>
<br />

<acme:cancel code="request.cancel" url="request/${role}/list.do"/>
