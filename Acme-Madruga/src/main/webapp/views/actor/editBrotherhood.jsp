
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

	<form:label path="title">
		<spring:message code="actor.title" />:
	</form:label>
	<form:input path="title" />
	<form:errors cssClass="error" path="title" />
	<br />

	<form:label path="establishment">
		<spring:message code="actor.establishment" />:
	</form:label>
	<form:input path="establishment" />
	<form:errors cssClass="error" path="establishment" />
	<br />

	<form:label path="pictures">
		<spring:message code="actor.pictures" />:
	</form:label>
	<form:input path="pictures" />
	<form:errors cssClass="error" path="pictures" />
	<br />

	<form:label path="name">
		<spring:message code="actor.name" />:
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	<br />

	<form:label path="middleName">
		<spring:message code="actor.middlename" />:
	</form:label>
	<form:input path="middleName" />
	<br />

	<form:label path="surname">
		<spring:message code="actor.surname" />:
	</form:label>
	<form:input path="surname" />
	<form:errors cssClass="error" path="surname" />
	<br />

	<form:label path="photo">
		<spring:message code="actor.photo" />:
	</form:label>
	<form:input path="photo" />
	<br />

	<form:label path="email">
		<spring:message code="actor.email" />:
	</form:label>
	<form:input path="email" />
	<form:errors cssClass="error" path="email" />
	<br />

	<form:label path="phone">
		<spring:message code="actor.phone" />:
	</form:label>
	<form:input path="phone" />
	<form:errors cssClass="error" path="phone" />
	<br />

	<form:label path="address">
		<spring:message code="actor.address" />:
	</form:label>
	<form:input path="address" />
	<form:errors cssClass="error" path="address" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="actor.save" />" />
	<spring:message code="actor.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('actor/display.do');" />
</form:form>