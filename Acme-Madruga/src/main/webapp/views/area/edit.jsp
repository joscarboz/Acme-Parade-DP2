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

<form:form action="area/administrator/edit.do"
	modelAttribute="area">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="area.name" path="name"/>
	<br />
	<acme:textarea code="area.pictures" path="pictures"/>
	<br />
	
	<acme:submit name="save" code="area.save" />
	<jstl:choose>
	<jstl:when test="${notUsed == true}">
		<acme:submit name="delete" code="area.delete" />
	</jstl:when>
	</jstl:choose>
	
	<acme:cancel code="area.cancel" url="area/administrator/list.do" />
	

</form:form>