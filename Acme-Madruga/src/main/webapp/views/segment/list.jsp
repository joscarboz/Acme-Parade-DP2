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

<display:table class="displaytag" name="segments"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="segment.origin" var="originHeader" />
	<display:column property="origin" title="${originHeader}"
		sortable="true" />

	<spring:message code="segment.destination" var="destinationHeader" />
	<display:column property="destination" title="${destinationHeader}"
		sortable="true" />

	<spring:message code="segment.originReachMoment"
		var="originReachMomentHeader" />
	<display:column property="originReachMoment"
		title="${originReachMomentHeader}" sortable="true" />

	<spring:message code="segment.destinationReachMoment"
		var="destinationReachMomentHeader" />
	<display:column property="destinationReachMoment"
		title="${destinationReachMomentHeader}" sortable="true" />



	<!-- Actions -->

	<display:column>
		<a
			href="segment/brotherhood/edit.do?segmentId=${row.id}&paradeId=${parade.id}">
			<spring:message code="segment.edit" />
		</a>
	</display:column>

	<display:column>
		<a href="segment/brotherhood/display.do?segmentId=${row.id}"> <spring:message
				code="segment.display" />
		</a>
	</display:column>

</display:table>


<jstl:if test="${!isParadeEmpty}">
	<a href="segment/brotherhood/delete.do?paradeId=${parade.id}"> <spring:message
			code="segment.delete" />
	</a>
	<a href="segment/brotherhood/clear.do?paradeId=${parade.id}"> <spring:message
			code="segment.clear" />
	</a>
</jstl:if>

<a href="segment/brotherhood/create.do?paradeId=${parade.id}"> <spring:message
		code="segment.create" />
</a>