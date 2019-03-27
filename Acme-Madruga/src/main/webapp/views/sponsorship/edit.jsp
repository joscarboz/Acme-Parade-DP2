
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

<form:form action="sponsorship/sponsor/edit.do"
	modelAttribute="sponsorshipForm">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="parade" />
	
	<b><spring:message code="sponsorship.sponsorship"/></b>
	<acme:textbox code="sponsorship.banner" path="banner" />
	<acme:textbox code="sponsorship.targetURL" path="targetUrl" />
	
	<b><spring:message code="sponsorship.creditCard"/></b>
	<acme:textbox code="sponsorship.holder" path="holder" />
	<acme:textbox code="sponsorship.make" path="make" />
	<acme:textbox code="sponsorship.number" path="number" />
	<acme:textbox code="sponsorship.expirationmonth" path="expirationMonth" />
	<acme:textbox code="sponsorship.expirationyear" path="expirationYear" />
	<acme:textbox code="sponsorship.cvv" path="cvv" />
	

	

	<input type="submit" name="save"
		value="<spring:message code="sponsorship.save" />" />
	<spring:message code="sponsorship.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('parade/sponsor/list.do?');" />


</form:form>