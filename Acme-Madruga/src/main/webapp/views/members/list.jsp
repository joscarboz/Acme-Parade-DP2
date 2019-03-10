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

<!--  Listing grid -->
<h1><spring:message code="enrolment.members"/></h1>
<display:table pagesize="5" class="displaytag" name="members"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="enrolment.member.name" var="nameHeader" />
	<display:column  title="${nameHeader}"
		sortable="true">
		<a href="member/display.do?memberId=${row.member.id}">${row.member.name}</a>
	</display:column>

	<spring:message code="enrolment.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" />


	<spring:message code="enrolment.position" var="positionHeader" />
	<spring:message code="enrolment.language" var="language" />
	<jstl:choose>
		<jstl:when test="${language == 'English'}">
			<display:column property="position.title" title="${positionHeader}"
				sortable="true" />
		</jstl:when>
		<jstl:when test="${language == 'Spanish'}">
			<display:column property="position.spanishTitle"
				title="${positionHeader}" sortable="true" />
		</jstl:when>
	</jstl:choose>

	<display:column>
				<a href="enrolment/brotherhood/dropOut.do?enrolmentId=${row.id}">
					<spring:message code="enrolment.dropOut" />
				</a>
	</display:column>
	</display:table>
	<br />


<h1><spring:message code="enrolment.enrolments"/></h1>
<display:table pagesize="5" class="displaytag" name="pastEnrolments"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="enrolment.member.name" var="nameHeader" />
	<display:column  title="${nameHeader}"
		sortable="true">
		<a href="member/display.do?memberId=${row.member.id}">${row.member.name}</a>
	</display:column>

	<spring:message code="enrolment.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" />
	
	<display:column>
	<jstl:choose>
	<jstl:when test="${hasArea==true}">
	<a href="enrolment/brotherhood/accept.do?enrolmentId=${row.id}"><spring:message
						code="enrolment.accept" /></a>
	</jstl:when>
	</jstl:choose>
	</display:column>
	







</display:table>

