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

<b><spring:message code="float.title" /></b>
<jstl:out value="${res.title}" />
<br />

<b><spring:message code="float.description" /></b>
<jstl:out value="${res.description}" />
<br />

<b><spring:message code="float.pictures" /></b>
<jstl:out value="${res.pictures}" />
<br />

<jstl:choose>
	<jstl:when test="${role == 'brotherhood'}">
		<acme:cancel code="float.cancel" url="float/${role}/list.do" />
	</jstl:when>
</jstl:choose>
