
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

<form:form action="actor/editBrotherhood.do"
	modelAttribute="editBrotherhoodForm">

	<form:hidden path="area" />

	<jstl:choose>
		<jstl:when test="${!areas.isEmpty()}">
			<form:label path="areas">
				<spring:message code="actor.areaWarning" />:
			</form:label>
			<br />
			<form:select path="areas">
				<form:options items="${areas}" />
			</form:select>
			<br />
		</jstl:when>
		<jstl:otherwise>

		</jstl:otherwise>
	</jstl:choose>

	<acme:textbox code="actor.title" path="title"/>

	<acme:textbox code="actor.establishment" path="establishment"/>
	
	<acme:textbox code="actor.pictures" path="pictures"/>
	
	<acme:textbox code="actor.name" path="name"/>
	
	<acme:textbox code="actor.middlename" path="middleName"/>
	
	<acme:textbox code="actor.surname" path="surname"/>

	<acme:textbox code="actor.photo" path="photo"/>
	
	<acme:textbox code="actor.email" path="email"/>
	
	<acme:textbox code="actor.phone" path="phone"/>
	
	<acme:textbox code="actor.address" path="address"/>

	<acme:submit name="save" code="actor.save"/>
		
	<acme:cancel url="actor/display.do" code="actor.cancel"/>	
</form:form>