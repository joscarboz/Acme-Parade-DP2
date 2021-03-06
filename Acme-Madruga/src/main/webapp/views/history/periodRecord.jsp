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

<form:form action="history/periodRecord/edit.do"
	modelAttribute="periodRecord">

	<form:hidden path="id" />
	<form:hidden path="version" />


	<acme:textbox code="record.title" path="title" />
	<br />
	<acme:textarea code="record.description" path="description" />
	<br />
	<acme:textarea code="record.pictures" path="pictures"/>
	<br />
	<acme:textbox code="record.startYear" path="startYear"/>
	<br />
	<acme:textbox code="record.endYear" path="endYear"/>
	<br />
	<acme:submit name="save" code="record.save" />
	<acme:cancel code="record.cancel"
		url="history/brotherhood/display.do" />


</form:form>