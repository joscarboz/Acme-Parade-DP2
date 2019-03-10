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

<display:table pagesize="5" class="displaytag" name="brotherhoods"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="brotherhood.name" var="brotherhoodHeader" />
	<display:column property="title" title="${brotherhoodHeader}" />

	<spring:message code="brotherhood.establishment"
		var="establishmentHeader" />
	<display:column property="establishment" title="${establishmentHeader}" />

	<spring:message code="brotherhood.area" var="areaHeader" />
	<display:column property="area.name" title="${areaHeader}" />

	<!-- Actions -->

	<display:column>
		<a href="brotherhood/display.do?brotherhoodId=${row.id}"> <spring:message
				code="brotherhood.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('MEMBER')">

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.area.name != 'defaultArea'}">
					<a href="enrolment/member/enrol.do?brotherhoodId=${row.id}"> <spring:message
							code="brotherhood.enrol" />
					</a>
				</jstl:when>
			</jstl:choose>
		</display:column>
	</security:authorize>

</display:table>

<br />