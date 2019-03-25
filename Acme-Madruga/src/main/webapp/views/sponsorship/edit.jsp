
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

<form:form action="sponsorship/sponsor/edit.do"
	modelAttribute="sponsorshipForm">

	<input type="hidden" name="paradeId" value="${paradeId}">
	<input type="hidden" name="sponsorship" value="${sponsorship}">
	<input type="hidden" name="creditCard" value="${creditCard}">

	<form:label path="banner">
		<spring:message code="sponsorship.banner" />:
	</form:label>
	<form:input path="banner" />
	<form:errors cssClass="error" path="banner" >
	</form:errors>
	<br />

	<form:label path="infoPage">
		<spring:message code="sponsorship.infoPage" />:
	</form:label>
	<form:input path="infoPage" />
	<form:errors cssClass="error" path="infoPage" />
	<br />


	<form:label path="holdername">
		<spring:message code="sponsorship.holdername" />:
	</form:label>
	<form:input path="holdername" />
	<form:errors cssClass="error" path="holdername" />
	<br />

	<form:label path="brandname">
		<spring:message code="sponsorship.brandname" />:
	</form:label>
	<form:input path="brandname" />
	<form:errors cssClass="error" path="brandname" />
	<br />

	<form:label path="number">
		<spring:message code="sponsorship.number" />:
	</form:label>
	<form:input path="number" />
	<form:errors cssClass="error" path="number" />
	<br />

	<form:label path="expirationmonth">
		<spring:message code="sponsorship.expirationmonth" />:
	</form:label>
	<form:input path="expirationmonth" />
	<form:errors cssClass="error" path="expirationmonth" />
	<br />

	<form:label path="expirationyear">
		<spring:message code="sponsorship.expirationyear" />:
	</form:label>
	<form:input path="expirationyear" />
	<form:errors cssClass="error" path="expirationyear" />
	<br />

	<form:label path="cvv">
		<spring:message code="sponsorship.cvv" />:
	</form:label>
	<form:input path="cvv" />
	<form:errors cssClass="error" path="cvv" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="sponsorship.save" />" />
	<spring:message code="sponsorship.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('parade/sponsor/list.do?');" />


</form:form>