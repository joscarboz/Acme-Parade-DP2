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

<form:form action="float/brotherhood/edit.do"
	modelAttribute="float">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="float.title" path="title"/>
	<br />
	<acme:textarea code="float.description" path="description"/>
	<br />
	<acme:textarea code="float.pictures" path="pictures"/>
	<br />
	
	<acme:submit name="save" code="float.save" />
	<jstl:choose>
	<jstl:when test="${id != 0}">
		<acme:submit name="delete" code="float.delete" />
	</jstl:when>
	</jstl:choose>
	
	<acme:cancel code="float.cancel" url="float/brotherhood/list.do" />
	

</form:form>