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

<b><spring:message code="member.name" /></b>
<jstl:out value="${member.name}" />
<br />

<b><spring:message code="member.middleName" /></b>
<jstl:out value="${member.middleName}" />
<br />

<b><spring:message code="member.surname" /></b>
<jstl:out value="${member.surname}" />
<br />
<security:authorize access="hasRole('BROTHERHOOD')">
<b><spring:message code="member.photo" /></b>
<jstl:out value="${member.photo}" />
<br />

<b><spring:message code="member.email" /></b>
<jstl:out value="${member.email}" />
<br />

<b><spring:message code="member.address" /></b>
<jstl:out value="${member.address}" />
<br />

<b><spring:message code="member.phone" /></b>
<jstl:out value="${member.phone}" />
<br />
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${member.spammer=='true'}">
		<spring:message code="actor.spammer" />
	</jstl:if>
	<br />
	<b><spring:message code="actor.score" /></b>	
	<jstl:out value="${member.score}"/>
	<br/>
</security:authorize>

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