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

<form:form action="parade/chapter/reject.do" modelAttribute="parade">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="status" />
	<form:hidden path="floats" />
	<form:hidden path="title" />
	<form:hidden path="requests" />
	<form:hidden path="segments" />
	<form:hidden path="ticker" />
	<form:hidden path="moment" />
	<form:hidden path="draftMode" />
	<form:hidden path="description" />
	<spring:message code="parade.rejectionReason"/>:
	<form:input code="parade.rejectionReason" path="rejectionReason" placeholder="Not blank"/>
	<br />
	<acme:submit name="save" code="parade.save" />

	<acme:cancel code="parade.cancel" url="parade/chapter/list.do" />
</form:form>