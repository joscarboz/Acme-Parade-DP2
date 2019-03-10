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

<form:form action="procession/brotherhood/edit.do"
	modelAttribute="procession">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="ticker" />
	<form:hidden path="requests" />

	<acme:textbox code="procession.title" path="title" />
	<br />
	<acme:textarea code="procession.description" path="description" />
	<br />
	<spring:message code="procession.moment"/>:
	<form:input code="procession.moment" path="moment" placeholder="dd/MM/yyyy HH:mm"/>
	<form:errors cssClass="error" path="moment" />
	<br />
	<form:label path="draftMode">
		<spring:message code="procession.draftMode" />:
	</form:label>
	<INPUT TYPE="radio" name="draftMode" value="true" checked="checked" />true
	<INPUT TYPE="radio" NAME="draftMode" value="false" />false
	<br />
	<acme:select code="procession.floats" path="floats" items="${floats}"
		itemLabel="title" id="floats" />
	<br />
	<acme:submit name="save" code="procession.save" />
	<acme:cancel code="procession.cancel"
		url="procession/brotherhood/list.do" />


</form:form>