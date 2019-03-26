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

<b><spring:message code="brotherhood.title" /></b>
<jstl:out value="${brotherhood.title}" />
<br />

<b><spring:message code="brotherhood.establishment" /></b>
<jstl:out value="${brotherhood.establishment}" />
<br />

<b><spring:message code="brotherhood.pictures" /></b>
<jstl:out value="${brotherhood.pictures}" />
<br />

<b><spring:message code="brotherhood.area" /></b>
<jstl:out value="${brotherhood.area.name}" />
<br />

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${brotherhood.spammer=='true'}">
		<spring:message code="actor.spammer" />
	</jstl:if>
	<br />
	<b><spring:message code="actor.score" /></b>
	<jstl:out value="${brotherhood.score}" />
	<br />
</security:authorize>

<!--  Listing grid -->

<b><spring:message code="brotherhood.members" /></b>

<display:table pagesize="5" class="displaytag" name="members"
	requestURI="member/list.do" id="row">

	<!-- Attributes -->

	<spring:message code="member.name" var="nameHeader" />
	<display:column title="${nameHeader}">
		<a href="member/display.do?memberId=${row.id}">${row.name}</a>
	</display:column>
	<spring:message code="member.middleName" var="middleNameHeader" />
	<display:column property="middleName" title="${middleNameHeader}" />

	<spring:message code="member.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" />

</display:table>
<br />
<!--  Listing grid -->

<b><spring:message code="brotherhood.floats" /></b>

<display:table pagesize="5" class="displaytag" name="floats"
	requestURI="float/list.do" id="row">

	<!-- Attributes -->
	<spring:message code="float.title" var="titleHeader" />
	<display:column title="${titleHeader}">
		<a href="float/display.do?floatId=${row.id}">${row.title}</a>
	</display:column>
	<spring:message code="float.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

</display:table>
<br />
<!--  Listing grid -->

<b><spring:message code="brotherhood.parades" /></b>

<display:table pagesize="5" class="displaytag" name="parades"
	requestURI="parade/list.do" id="row">

	<!-- Attributes -->

	<spring:message code="parade.ticker" var="tickerHeader" />
	<display:column title="${tickerHeader}">
		<a href="parade/display.do?paradeId=${row.id}">${row.ticker}</a>
	</display:column>

	<spring:message code="parade.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="parade.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

	<spring:message code="parade.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />

</display:table>
<br />

<b><spring:message code="brotherhood.socialProfiles" /></b>

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

<h2>
	<spring:message code="brotherhood.history" />
</h2>
<br />

<b><spring:message code="brotherhood.inceptionRecord" /></b>

<display:table pagesize="5" class="displaytag" name="history.inceptionRecord"
	requestURI="parade/list.do" id="row">

	<!-- Attributes -->


	<spring:message code="record.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="record.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

	<spring:message code="record.pictures" var="picturesHeader" />
	<display:column property="pictures" title="${picturesHeader}" />

</display:table>
<br />

<b><spring:message code="brotherhood.legalRecords" /></b>

<display:table pagesize="5" class="displaytag" name="history.legalRecords"
	requestURI="parade/list.do" id="row">

	<!-- Attributes -->


	<spring:message code="record.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="record.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

	<spring:message code="record.laws" var="lawsHeader" />
	<display:column property="laws" title="${lawsHeader}" />

	<spring:message code="record.legalName" var="legalNameHeader" />
	<display:column property="legalName" title="${legalNameHeader}" />

	<spring:message code="record.VAT" var="VATHeader" />
	<display:column property="VAT" title="${VATHeader}" />

</display:table>
<br />

<b><spring:message code="brotherhood.linkRecords" /></b>
<display:table pagesize="5" class="displaytag" name="history.linkRecords"
	requestURI="parade/list.do" id="row">

	<!-- Attributes -->


	<spring:message code="record.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="record.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

	<spring:message code="record.link" var="linkHeader" />
	<display:column title="${linkHeader}">
		<a href="brotherhood/display.do?brotherhoodId=${row.link.id}">${row.link.title}</a>
	</display:column>

</display:table>
<br />
<b><spring:message code="brotherhood.miscellaneousRecords" /></b>
<display:table pagesize="5" class="displaytag"
	name="history.miscellaneousRecords" requestURI="parade/list.do" id="row">

	<!-- Attributes -->


	<spring:message code="record.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="record.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />


</display:table>
<br />

<b><spring:message code="brotherhood.periodRecords" /></b>
<display:table pagesize="5" class="displaytag" name="history.periodRecords"
	requestURI="parade/list.do" id="row">

	<!-- Attributes -->


	<spring:message code="record.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />

	<spring:message code="record.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />

	<spring:message code="record.pictures" var="picturesHeader" />
	<display:column property="pictures" title="${picturesHeader}" />

	<spring:message code="record.startYear" var="startYearHeader" />
	<display:column property="startYear" title="${startYearHeader}" />

	<spring:message code="record.endYear" var="endYearHeader" />
	<display:column property="endYear" title="${endYearHeader}" />

</display:table>
<br />



