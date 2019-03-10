
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

<form:form action="actor/registerAdministrator.do" modelAttribute="registerAdminForm">
	
	<br/>
	<form:label path="username">
		<spring:message code="actor.username" />:
	</form:label>
	<form:input path="username" />
	<form:errors cssClass="error" path="username" />
	<br />

	<form:label path="password">
		<spring:message code="actor.password" />:
	</form:label>
	<form:password path="password" />
	<form:errors cssClass="error" path="password" />
	<br />

	<form:label path="password2">
		<spring:message code="actor.password2" />:
	</form:label>
	<form:password path="password2" />
	<form:errors cssClass="error" path="password2" />
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
	<form:errors cssClass="error" path="middleName" />
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
	<form:errors cssClass="error" path="photo" />
	<br />

	<form:label path="email">
		<spring:message code="actor.email" />:
	</form:label>
	<form:input path="email" />
	<form:errors cssClass="error" path="email" />
	<br />

	<form:label path="address">
		<spring:message code="actor.address" />:
	</form:label>
	<form:input path="address" />
	<form:errors cssClass="error" path="address" />
	<br />

	<form:label path="phone">
		<spring:message code="actor.phone" />:
	</form:label>
	<form:input path="phone" />
	<form:errors cssClass="error" path="phone" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="actor.save" />" />
	<spring:message code="actor.cancel" var="cancel" />

</form:form>
<spring:message code="actor.phoneCheck" var="phoneCheck" />
<script>
	$('#registerAdminForm').submit(function() {
		var regex = /^((\+[1-9][0-9]{0,2}\s[0-9]{4,})|(\+[1-9][0-9]{0,2}\s\([1-9][0-9]{0,2}\)\s[0-9]{4,})|([0-9]{4,})|(^$))$/;
		var phone = document.getElementById("phone").value;
		var submit = false;
		if (!regex.exec(phone)) {
			submit = confirm('<jstl:out value="${phoneCheck}" />');
		} else {
			submit = true;
		}
		return submit;
	});
</script>