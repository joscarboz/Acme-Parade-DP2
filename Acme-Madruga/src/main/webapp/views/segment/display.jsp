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

<b><spring:message code="segment.origin" /></b>
<jstl:out value="${segment.origin}" />
<br />

<b><spring:message code="segment.destination" /></b>
<jstl:out value="${segment.destination}" />
<br />

<b><spring:message code="segment.originReachMoment" /></b>
<jstl:out value="${segment.originReachMoment}" />
<br />

<b><spring:message code="segment.destinationReachMoment"/></b>
<jstl:out value="${segment.destinationReachMoment}" />
<br />


