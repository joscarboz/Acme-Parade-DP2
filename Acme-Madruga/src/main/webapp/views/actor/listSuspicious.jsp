<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!--  Listing grid -->

<display:table class="displaytag" name="actors" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<spring:message code="actor.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="false"/>
	
	<spring:message code="actor.middlename" var="middlenameHeader" />
	<display:column property="middleName" title="${middlenameHeader}" sortable="false"/>
	
	<spring:message code="actor.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" sortable="false"/>
	
	<spring:message code="actor.userAccount.username" var="usernameHeader" />
	<display:column property="userAccount.username" title="${usernameHeader}" sortable="false"/>
	
	<display:column>
	<jstl:if test="${row.userAccount.accountNonLocked=='true'}">
		<a href="actor/administrator/ban.do?actorId=${row.id}"><spring:message code="actor.ban"></spring:message></a>
	</jstl:if>
	<jstl:if test="${row.userAccount.accountNonLocked=='false'}">
		<a href="actor/administrator/unban.do?actorId=${row.id}"><spring:message code="actor.unban"></spring:message></a>
	</jstl:if>
	</display:column>
	
</display:table>


