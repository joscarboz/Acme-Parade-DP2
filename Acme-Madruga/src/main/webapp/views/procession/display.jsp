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

<b><spring:message code="procession.ticker" /></b>
<jstl:out value="${procession.ticker}" />
<br />

<b><spring:message code="procession.title" /></b>
<jstl:out value="${procession.title}" />
<br />

<b><spring:message code="procession.description" /></b>
<jstl:out value="${procession.description}" />
<br />

<b><spring:message code="procession.moment" /></b>
<jstl:out value="${procession.moment}" />
<br />

<b><spring:message code="procession.draftMode" /></b>
<jstl:out value="${procession.draftMode}" />
<br />

<b><spring:message code="procession.floats" /></b>
<display:table pagesize="5" class="displaytag" name="floats"
	requestURI="float/list.do" id="row">
	<spring:message code="float.title" var="titleHeader" />
	<display:column title="${titleHeader}" sortable="false">
		<a href="float/display.do?floatId=${row.id}">${row.title}</a>
	</display:column>

	<spring:message code="float.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />
</display:table>
<br />


<jstl:choose>
	<jstl:when test="${role == 'brotherhood'}">
		<acme:cancel code="procession.cancel" url="procession/${role}/list.do" />
	</jstl:when>
</jstl:choose>


