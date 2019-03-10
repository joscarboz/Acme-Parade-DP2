<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<b><spring:message code="message.subject"/></b>
<jstl:out value="${displayedmessage.subject}"/>
<br/>

<b><spring:message code="message.body"/></b>
<jstl:out value="${displayedmessage.body}"/>
<br/>


<b><spring:message code="message.moment"/></b>
<fmt:formatDate value="${displayedmessage.moment}" pattern="yyyy/MM/dd"/>
<br/>

<b><spring:message code="message.priority"/></b>
<jstl:out value="${displayedmessage.priority}"/>
<br/>

<b><spring:message code="message.tags"/></b>
<br/>
<jstl:forEach var="tag" items="${displayedmessage.tags}">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="${tag}"><jstl:out value="${tag}"/></a>
	<br/>
</jstl:forEach>
<br/>

<b><spring:message code="message.sender"/></b>
<jstl:out value="${displayedmessage.sender.userAccount.username}"/>
<br/>

<b><spring:message code="message.recipient"/></b>
<jstl:out value="${displayedmessage.recipients[0].userAccount.username}"/>
<br/>



