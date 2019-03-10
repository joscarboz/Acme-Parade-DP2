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

<form:form action="position/administrator/edit.do"
	modelAttribute="position">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="position.title" path="title"/>
	<br />
	<acme:textbox code="position.spanishTitle" path="spanishTitle"/>
	<br />
	
	<acme:submit name="save" code="position.save" />
	<jstl:choose>
	<jstl:when test="${notUsed == true}">
		<acme:submit name="delete" code="position.delete" />
	</jstl:when>
	</jstl:choose>
	
	<acme:cancel code="position.cancel" url="position/administrator/list.do" />
	

</form:form>