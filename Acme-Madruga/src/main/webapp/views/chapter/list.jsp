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

<security:authorize access="hasRole('CHAPTER')">
	<display:table pagesize="5" class="displaytag" name="parades"
		requestURI="${requestURI}" id="row">



		<spring:message code="parade.ticker" var="tickerHeader" />
		<display:column title="${tickerHeader}" sortable="false">
			<a href="parade/display.do?paradeId=${row.id}">${row.ticker}</a>
		</display:column>

		<spring:message code="parade.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="false" />

		<spring:message code="parade.moment" var="momentHeader" />
		<display:column property="moment" title="${momentHeader}"
			sortable="false" />
			
		<spring:message code="parade.status" var="statusHeader" />
		<display:column property="status" title="${statusHeader}"
			sortable="true" />


		<!-- Actions -->

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.status == 'submitted' }">
					<a href="parade/chapter/accept.do?paradeId=${row.id}"> <spring:message
							code="parade.accept" />
					</a>
				</jstl:when>
			</jstl:choose>
		</display:column>

		<display:column>
			<jstl:choose>
				<jstl:when test="${row.status == 'submitted' }">
					<a href="parade/chapter/reject.do?paradeId=${row.id}"> <spring:message
							code="parade.reject" />
					</a>
				</jstl:when>
			</jstl:choose>
		</display:column>



		<br />
	</display:table>
</security:authorize>



<br />
<br />