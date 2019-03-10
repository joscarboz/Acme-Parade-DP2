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


<b><spring:message code="actor.title" /></b>
<jstl:out value="${actor.title}" />
<br />

<b><spring:message code="actor.establishment" /></b>
<jstl:out value="${actor.establishment}" />
<br />

<b><spring:message code="actor.pictures" /></b>
<jstl:out value="${actor.pictures}" />
<br />

<b><spring:message code="actor.area.name" /></b>
<jstl:out value="${actor.area.name}" />
<br />

<b><spring:message code="actor.area.pictures" /></b>
<jstl:out value="${actor.area.pictures}" />
<br />


<b><spring:message code="actor.name" /></b>
<jstl:out value="${actor.name}" />
<br />

<b><spring:message code="actor.surname" /></b>
<jstl:out value="${actor.surname}" />
<br />

<b><spring:message code="actor.middleName" /></b>
<jstl:out value="${actor.middleName}" />
<br />

<b><spring:message code="actor.photo" /></b>
<jstl:out value="${actor.photo}" />
<br />

<b><spring:message code="actor.email" /></b>
<jstl:out value="${actor.email}" />
<br />

<b><spring:message code="actor.phone" /></b>
<jstl:out value="${actor.phone}" />
<br />

<b><spring:message code="actor.address" /></b>
<jstl:out value="${actor.address}" />
<br />

<b><spring:message code="actor.userAccount.username" /></b>
<jstl:out value="${actor.userAccount.username}" />
<br />



<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${actor.spammer=='true'}">
		<spring:message code="actor.spammer" />
	</jstl:if>
	<br />
	<b><spring:message code="actor.score" /></b>
	<jstl:out value="${actor.score}" />
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
	<jstl:choose>
		<jstl:when test="${actor2 ==null}">
			<display:column>
				<a href="socialProfile/edit.do?socialProfileId=${row.id}"> <spring:message
						code="socialProfile.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="socialProfile/delete.do?socialProfileId=${row.id}"> <spring:message
						code="socialProfile.delete" />
				</a>
			</display:column>
		</jstl:when>
	</jstl:choose>
</display:table>
<br />
<jstl:choose>
	<jstl:when test="${actor2 ==null}">
		<a href="socialProfile/create.do"> <spring:message
				code="socialProfile.create" />
		</a>
	</jstl:when>
</jstl:choose>
<br />




