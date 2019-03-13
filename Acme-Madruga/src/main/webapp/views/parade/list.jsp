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

<display:table pagesize="5" class="displaytag" name="parades"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->

	<spring:message code="parade.ticker" var="tickerHeader" />
	<display:column title="${tickerHeader}" sortable="false">
		<a href="parade/display.do?paradeId=${row.id}">${row.ticker}</a>
	</display:column>

	<spring:message code="parade.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="parade.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<spring:message code="parade.draftMode" var="draftModeHeader" />
		<display:column property="draftMode" title="${draftModeHeader}"
			sortable="true" />


		<!-- Actions -->

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.draftMode == true }">
					<a href="parade/brotherhood/edit.do?paradeId=${row.id}">
						<spring:message code="parade.edit" />
					</a>
				</jstl:when>
			</jstl:choose>
		</display:column>

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.draftMode == true }">
					<display:column>
						<a href="parade/brotherhood/delete.do?paradeId=${row.id}">
							<spring:message code="parade.delete" />
						</a>
					</display:column>
				</jstl:when>
			</jstl:choose>
		</display:column>

		<br />

	</security:authorize>

	<security:authorize access="hasRole('MEMBER')">
		<display:column>
			<a href="request/member/create.do?paradeId=${row.id}"> <spring:message
					code="parade.request" />
			</a>
		</display:column>
	</security:authorize>

</display:table>


<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:choose>
		<jstl:when test="${hasArea==true}">
			<a href="parade/brotherhood/create.do"> <spring:message
					code="parade.create" /></a>
		</jstl:when>
		<jstl:otherwise>
		<h1><spring:message code="parade.noArea"/></h1>
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>

<br />
<br />