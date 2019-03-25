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

<b><spring:message code="chapter.title" /></b>
<jstl:out value="${chapter.title}" />
<br />

<b><spring:message code="chapter.area" /></b>
<jstl:out value="${chapter.area.name}" />
<br />

<b><spring:message code="chapter.name" /></b>
<jstl:out value="${chapter.name}" />
<br />

<b><spring:message code="chapter.middleName" /></b>
<jstl:out value="${chapter.middleName}" />
<br />
<b><spring:message code="chapter.surname" /></b>
<jstl:out value="${chapter.surname}" />
<br />

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${chapter.spammer=='true'}">
		<spring:message code="chapter.spammer" />
	</jstl:if>
	<br />
	<b><spring:message code="actor.score" /></b>
	<jstl:out value="${chapter.score}" />
	<br />
</security:authorize>

<!--  Listing grid -->

<h2><spring:message code="brotherhood" /></h2>

<display:table pagesize="5" class="displaytag" name="brotherhoods"
	requestURI="brotherhood/list.do" id="row">

	<!-- Attributes -->

	<spring:message code="brotherhood.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
	<display:column title="${nameHeader}">
		<a href="brotherhood/display.do?brotherhoodId=${row.id}">${row.name}</a>
	</display:column>
	<spring:message code="brotherhood.establishment"
		var="establishmentHeader" />
	<display:column property="establishment" title="${establishmentHeader}" />

	<spring:message code="brotherhood.pictures" var="picturesHeader" />
	<display:column property="pictures" title="${picturesHeader}" />

</display:table>
<br />


<h2><spring:message code="chapter.socialProfiles" /></h2>

<display:table pagesize="5" class="displaytag" name="socialProfiles"
	requestURI="${requestURI}" id="row">
	<spring:message code="socialProfile.nick" var="nickHeader" />
	<display:column property="nick" title="${nickHeader}" sortable="false" />

	<spring:message code="socialProfile.socialNetwork" var="snHeader" />
	<display:column property="socialNetwork" title="${snHeader}"
		sortable="false" />

	<spring:message code="socialProfile.profile" var="profileHeader" />
	<display:column property="profileLink" title="${profileHeader}"
		sortable="false" />

</display:table>

<br />



