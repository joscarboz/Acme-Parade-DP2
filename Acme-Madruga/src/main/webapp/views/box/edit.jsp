
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

<form:form action="box/edit.do" modelAttribute="box">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="custom"/>


	<form:label path="title">
		<spring:message code="box.title" />:
	</form:label>
	<form:input path="title" />
	<form:errors cssClass="error" path="title" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="box.save" />" />
	<spring:message code="box.cancel" var="cancel" />
	<input type="button" name="cancel" value="${cancel}"
		onclick="javascript:relativeRedir('message/list.do?boxName=in box');" />


</form:form>