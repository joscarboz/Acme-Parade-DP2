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


<jstl:if test="${sponsorship != null}">
<img src="${sponsorship.banner}" height="100" width="100" alt="${sponsorship.banner}">
</jstl:if>

<br/>

<b><spring:message code="parade.ticker" /></b>
<jstl:out value="${parade.ticker}" />
<br />

<b><spring:message code="parade.title" /></b>
<jstl:out value="${parade.title}" />
<br />

<b><spring:message code="parade.description" /></b>
<jstl:out value="${parade.description}" />
<br />

<b><spring:message code="parade.moment" /></b>
<jstl:out value="${parade.moment}" />
<br />

<b><spring:message code="parade.draftMode" /></b>
<jstl:out value="${parade.draftMode}" />
<br />

<jstl:choose>
	<jstl:when test="${parade.status == 'rejected' }">
		<b><spring:message code="parade.rejectionReason" /></b>
		<jstl:out value="${parade.rejectionReason }" />
		<br />
	</jstl:when>
</jstl:choose>


<b><spring:message code="parade.floats" /></b>
<display:table pagesize="5" class="displaytag" name="floats"
	requestURI="float/list.do" id="row">
	<spring:message code="float.title" var="titleHeader" />
	<display:column title="${titleHeader}" sortable="false">
		<a href="float/display.do?floatId=${row.id}">${row.title}</a>
	</display:column>

	<spring:message code="float.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />
	<security:authorize access="hasRole('SPONSOR')">
		<display:column>
			<a href="sponsorship/sponsor/create.do?paradeId=${parade.id}"> <spring:message
					code="sponsorship.create" />
			</a>
		</display:column>
	</security:authorize>

</display:table>
<br />




<jstl:choose>
	<jstl:when test="${role == 'brotherhood'}">
		<acme:cancel code="parade.cancel" url="parade/${role}/list.do" />
	</jstl:when>
</jstl:choose>


