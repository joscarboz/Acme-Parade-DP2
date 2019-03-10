<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!--  Listing grid -->


<h1>
	<spring:message code="message.titleBox" />
	${boxName}
</h1>
<br />

<a href="message/create.do?"><spring:message code="message.create"></spring:message></a>
<br />


<display:table pagesize="5" class="displaytag" name="messages"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->

	<spring:message code="message.body" var="bodyHeader" />
	<display:column property="body" title="${bodyHeader}" sortable="false" />

	<spring:message code="message.subject" var="subjectHeader" />
	<display:column property="subject" title="${subjectHeader}"
		sortable="false" />

	<spring:message code="message.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="false" format="{0,date,yyyy/MM/dd HH:mm}" />

	<spring:message code="message.priority" var="priorityHeader" />
	<display:column property="priority" title="${priorityHeader}"
		sortable="false" />

	<spring:message code="message.sender" var="senderHeader" />
	<display:column property="sender.userAccount.username"
		title="${senderHeader}" sortable="false" />

	<spring:message code="message.recipient" var="recipientHeader" />
	<display:column property="recipients[0].userAccount.username"
		title="${recipientHeader}" sortable="false" />
	<display:column>
		<a href="message/display.do?messageId=${row.id}"><spring:message
				code="message.display"></spring:message></a>
	</display:column>
	<display:column>
		<a href="message/delete.do?messageId=${row.id}"><spring:message
				code="message.delete"></spring:message></a>
	</display:column>

	<display:column>
		<a href="message/boxmove.do?messageId=${row.id}"><spring:message
				code="message.move"></spring:message></a>
	</display:column>

</display:table>

<display:table class="displaytag" name="boxes"
	requestURI="${requestURI}" id="row">
	<display:column>
		<a href="message/list.do?boxName=${row.title}"><spring:message
				text="${row.title}"></spring:message></a>

	</display:column>

	<display:column>
		<jstl:if test="${row.custom}">
			<a href="box/edit.do?boxId=${row.id}"><spring:message
					code="message.box.edit"></spring:message></a>
		</jstl:if>
	</display:column>
	<display:column>
		<jstl:if test="${row.custom}">
			<a href="box/delete.do?boxId=${row.id}"><spring:message
					code="message.box.delete"></spring:message></a>
		</jstl:if>

	</display:column>


</display:table>

<form:form action="message/createbox.do" modelAttribute="box">
	<form:hidden path="id" />
	<form:hidden path="custom" />
	<form:hidden path="boxes" />
	<form:label path="title">
		<spring:message code="message.box.create" />:
	</form:label>
	<form:input path="title" />
	<form:errors cssClass="error" path="title" />
	<input type="submit" name="savebox"
		value="<spring:message code="message.box.create" />" />
</form:form>

