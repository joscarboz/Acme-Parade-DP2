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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="enrolment/brotherhood/accept.do"
	modelAttribute="enrolment">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="member" />
	<form:hidden path="brotherhood" />
	<form:hidden path="moment" />
	
	<spring:message code="enrolment.position" var="positionHeader" />
	<spring:message code="enrolment.language" var="language"/>	
	<jstl:choose>
		<jstl:when test="${language == 'English'}">
			<acme:select code="enrolment.position" path="position"
		items="${position}" itemLabel="title" id="position" />
		</jstl:when>
		<jstl:when test="${language == 'Spanish'}">
			<acme:select code="enrolment.position" path="position"
		items="${position}" itemLabel="spanishTitle" id="position" />
		</jstl:when>
	</jstl:choose>
	<acme:submit name="save" code="enrolment.accept" />
	<acme:cancel code="enrolment.cancel"
		url="enrolment/brotherhood/list.do" />


</form:form>