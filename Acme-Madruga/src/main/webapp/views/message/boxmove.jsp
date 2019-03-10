
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

<form:form action="message/boxmove.do" modelAttribute="boxMoveForm">


	<form:hidden path="messageId"/>
	<spring:message code="fixUpTask.category" />:
  <form:select path="title">
    <form:options items="${boxes}" itemLabel="title" itemValue="title"/>
  </form:select>
	<br />

	<input type="submit" name="save"
		value="<spring:message code="message.save" />" />
	<spring:message code="message.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('message/list.do?boxName=in box');" />


</form:form>