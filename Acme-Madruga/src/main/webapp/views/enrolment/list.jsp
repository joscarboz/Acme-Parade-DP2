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

<!--  Listing grid -->

<h1><spring:message code="enrolment.myBrotherhoods"/></h1>
<display:table pagesize="5" class="displaytag" name="enrolments"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="enrolment.brotherhood.name" var="brotherhoodHeader" />
	<display:column property="brotherhood.title" title="${brotherhoodHeader}"
		sortable="true" />
	
	<spring:message code="enrolment.position" var="positionHeader" />
	<display:column property="position.title" title="${positionHeader}"
		sortable="true" />
		
	<!-- Actions -->
	
	<display:column>
		<a href="enrolment/member/dropOut.do?enrolmentId=${row.id}"> <spring:message
				code="enrolment.dropOut" />
		</a>
	</display:column>
	
</display:table>
<br />

<h1><spring:message code="enrolment.myPastBrotherhoods"/></h1>
<display:table pagesize="5" class="displaytag" name="pastEnrolments"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="enrolment.brotherhood.name" var="brotherhoodHeader" />
	<display:column property="brotherhood.name" title="${brotherhoodHeader}"
		sortable="true" />
	
	<spring:message code="enrolment.dropOutDate" var="dropOutDateHeader" />
	<display:column property="dropOutDate" title="${dropOutDateHeader}"
		sortable="true" />
		
	
</display:table>

<br/>