
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

<form:form action="message/create.do" modelAttribute="messageForm">



	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br />


	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br />

	<spring:message code="message.priority" />:
  <form:select path="priority">
		<option value="HIGH">HIGH</option>
		<option value="MEDIUM">MEDIUM</option>
		<option value="LOW">LOW</option>
	</form:select>
	<br />

	<form:label path="recipient">
		<spring:message code="message.recipient" />:
	</form:label>
	<form:textarea path="recipient" />
	<form:errors cssClass="error" path="recipient" />
	<br />

	<form:label path="tags">
		<spring:message code="message.tags" />:
	</form:label>
	<form:textarea path="tags" />
	<form:errors cssClass="error" path="tags" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="message.save" />" />
	<spring:message code="message.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('message/list.do?boxName=in box');" />


</form:form>