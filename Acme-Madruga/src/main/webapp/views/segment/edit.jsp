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

<form:form action="segment/brotherhood/edit.do" modelAttribute="segmentForm">

	<form:hidden path="id" />
	<form:hidden path="version"/>
	<form:hidden path="paradeId"/>

	<spring:message code="segment.latitude" var="latitude" />
	<spring:message code="segment.longitude" var="longitude" />

	<jstl:if test="${segmentForm.id==0 && isParadeEmpty}">
		<acme:textbox code="segment.origin" path="origin"
			placeholder="${latitude},${longitude}" />
		<br />
		<acme:textbox code="segment.destination" path="destination"
			placeholder="${latitude},${longitude}" />
		<br />
		<acme:textbox code="segment.originReachMoment"
			path="originReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />
		<acme:textbox code="segment.destinationReachMoment"
			path="destinationReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />
	</jstl:if>

	<jstl:if test="${segmentForm.id==0 && !isParadeEmpty}">

		<form:hidden path="origin" />

		<acme:textbox code="segment.destination" path="destination"
			placeholder="${latitude},${longitude}" />
		<br />
		<acme:textbox code="segment.originReachMoment"
			path="originReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />
		<acme:textbox code="segment.destinationReachMoment"
			path="destinationReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />

	</jstl:if>

	<jstl:if test="${segmentForm.id!=0 && isLastSegment}">

		<form:hidden path="origin" />

		<acme:textbox code="segment.destination" path="destination"
			placeholder="${latitude},${longitude}" />
		<br />
		<acme:textbox code="segment.originReachMoment"
			path="originReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />
		<acme:textbox code="segment.destinationReachMoment"
			path="destinationReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />

	</jstl:if>

	<jstl:if test="${segmentForm.id!=0 && !isLastSegment}">

		<form:hidden path="origin" />
		<form:hidden path="destination" />
		
		<acme:textbox code="segment.originReachMoment"
			path="originReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />
		<acme:textbox code="segment.destinationReachMoment"
			path="destinationReachMoment" placeholder="dd/MM/yyyy HH:mm" />
		<br />

	</jstl:if>

	<acme:submit name="save" code="segment.save" />
	<acme:cancel code="parade.cancel" url="segment/brotherhood/list.do?paradeId=${segmentForm.paradeId}" />

</form:form>