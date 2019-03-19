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

<form:form action="request/${role}/edit.do"
	modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="status" />

	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:choose>
		<jstl:when test="${request.status == 'REJECTED'}">
			<acme:textarea code="request.rejectionReason" path="rejectionReason"/>
			<br />
		</jstl:when>
		<jstl:otherwise>
			<acme:textbox code="request.row" path="row"/>
			<br />
			<acme:textbox code="request.column" path="column"/>
			<br />
		</jstl:otherwise>
	</jstl:choose>
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
	<jstl:choose>
		<jstl:when test="${request.id == 0}">
			<acme:select code="request.parade" path="parade"
				items="${parades}" itemLabel="ticker" />
		</jstl:when>
	</jstl:choose>
	</security:authorize>
	
	
	<acme:submit name="save" code="request.save" />
	
	<acme:cancel code="request.cancel" url="request/${role}/list.do" />
	

</form:form>