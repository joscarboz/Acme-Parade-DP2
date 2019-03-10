
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="socialProfile/edit.do"
	modelAttribute="socialProfile">

	<form:hidden path="id" />
	<form:hidden path="version" />


	<form:label path="nick">
		<spring:message code="socialProfile.nick" />:
	</form:label>
	<form:input path="nick" />
	<form:errors cssClass="error" path="nick" />
	<br />


	<form:label path="socialNetwork">
		<spring:message code="socialProfile.socialNetwork" />:
	</form:label>
	<form:textarea path="socialNetwork" />
	<form:errors cssClass="error" path="socialNetwork" />
	<br />
	
	<form:label path="profileLink">
		<spring:message code="socialProfile.profile" />:
	</form:label>
	<form:textarea path="profileLink" />
	<form:errors cssClass="error" path="profileLink" />
	<br />
	
	<input type="submit" name="save"
		value="<spring:message code="socialProfile.save" />" />
	<spring:message code="socialProfile.cancel" var="cancel"/>
	<input type="button" name="cancel" value="${cancel}" onclick="javascript:relativeRedir('actor/display.do');" />
	
</form:form>