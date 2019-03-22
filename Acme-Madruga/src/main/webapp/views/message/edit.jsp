
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

<form:form action="message/create.do" modelAttribute="messageForm">

	<acme:textbox code="message.subject" path="subject"/>
	
	<acme:textarea code="message.body" path="body"/>

	<spring:message code="message.priority" />:
  <form:select path="priority">
		<option value="HIGH">HIGH</option>
		<option value="MEDIUM">MEDIUM</option>
		<option value="LOW">LOW</option>
	</form:select>
	<br />
	
	<acme:textarea code="message.recipient" path="recipient"/>

	<acme:textarea code="message.tags" path="tags"/>
	
	<acme:submit name="save" code="message.save"/>
		
	<acme:cancel url="message/list.do?boxName=in box" code="message.cancel"/>	


</form:form>