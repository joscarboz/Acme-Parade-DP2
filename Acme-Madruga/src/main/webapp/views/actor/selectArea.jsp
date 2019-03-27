
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

<form:form action="actor/selectArea.do"
	modelAttribute="selectAreaForm">

	<jstl:choose>
		<jstl:when test="${!areas.isEmpty()}">
			<form:label path="areas">
				<spring:message code="actor.areaWarning" />:
			</form:label>
			<br />
			<form:select path="areas">
				<form:options items="${areas}" />
			</form:select>
			<br />
		</jstl:when>
		<jstl:otherwise>

		</jstl:otherwise>
	</jstl:choose>
	
	<br/>
	
	<acme:submit name="save" code="actor.save"/>
		
	<acme:cancel url="/" code="actor.cancel"/>	
</form:form>