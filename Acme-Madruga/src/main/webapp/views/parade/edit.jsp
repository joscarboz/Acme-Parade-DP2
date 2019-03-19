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

<form:form action="parade/brotherhood/edit.do"
	modelAttribute="parade">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="ticker" />
	<form:hidden path="requests" />
	<form:hidden path="status" />
	

	<acme:textbox code="parade.title" path="title" />
	<br />
	<acme:textarea code="parade.description" path="description" />
	<br />
	<spring:message code="parade.moment"/>:
	<form:input code="parade.moment" path="moment" placeholder="dd/MM/yyyy HH:mm"/>
	<form:errors cssClass="error" path="moment" />
	<br />
	<form:label path="draftMode">
		<spring:message code="parade.draftMode" />:
	</form:label>
	<INPUT TYPE="radio" name="draftMode" value="true" checked="checked" />true
	<INPUT TYPE="radio" NAME="draftMode" value="false" />false
	<br />
	<acme:select code="parade.floats" path="floats" items="${floats}"
		itemLabel="title" id="floats" />
	<br />
	<acme:submit name="save" code="parade.save" />
	<acme:cancel code="parade.cancel"
		url="parade/brotherhood/list.do" />


</form:form>