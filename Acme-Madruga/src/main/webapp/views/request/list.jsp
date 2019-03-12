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

<display:table pagesize="5" class="displaytag" name="requests"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->

	<jstl:choose>
		<jstl:when test="${row.status=='ACCEPTED'}">
			<spring:message code="request.status" var="statusHeader" />
			<display:column property="status" title="${statusHeader}"
				sortable="true" style="background-color:#3cea71" />
		</jstl:when>
		<jstl:when test="${row.status=='REJECTED'}">
			<spring:message code="request.status" var="statusHeader" />
			<display:column property="status" title="${statusHeader}"
				sortable="true" style="background-color:#f95036" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="request.status" var="statusHeader" />
			<jstl:choose>
				<jstl:when test="${row.parade.moment < date}">
					<display:column property="status" title="${statusHeader}"
						style="background-color:#9c9c9c" sortable="true" />
				</jstl:when>
				<jstl:otherwise>
					<display:column property="status" title="${statusHeader}"
						style="background-color:initial" sortable="true" />
				</jstl:otherwise>
			</jstl:choose>
		</jstl:otherwise>
	</jstl:choose>
	
	<jstl:choose>
	<jstl:when test="${row.status != 'REJECTED'}">
		<spring:message code="request.row" var="rowHeader" />
		<display:column property="row" title="${rowHeader}"
			sortable="false" />

		<spring:message code="request.column" var="columnHeader" />
		<display:column property="column" title="${columnHeader}"
			sortable="false" />
	</jstl:when>
	</jstl:choose>
	
	<display:column>
	<a href="parade/display.do?paradeId=${row.parade.id }">
	<jstl:out value="${row.parade.ticker}"/>
	</a> 
	</display:column>
	
	<display:column>
	<jstl:out value="${row.member.name}"/>
	</display:column>
	<display:column>
	<jstl:out value="${row.member.surname}"/>
	</display:column>
	
	<!-- Accept / Reject a request -->
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:choose>
			<jstl:when test="${row.status=='PENDING'}">
				<display:column>
				<a href="request/brotherhood/accept.do?requestId=${row.id}">
					<spring:message code="request.accept" />
				</a>
				</display:column>
				<br></br>
				<display:column>
				<a href="request/brotherhood/reject.do?requestId=${row.id}">
					<spring:message code="request.reject" />
				</a>
				</display:column>
			</jstl:when>
		</jstl:choose>
	</security:authorize>
	
	<!-- Display a request -->

	<display:column>
		<a href="request/${role}/display.do?requestId=${row.id}"> <spring:message
				code="request.display" />
		</a>
	</display:column>
</display:table>
