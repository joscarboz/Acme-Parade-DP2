
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="finder/member/edit.do"
	modelAttribute="finder">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="finderTime"/>
	
	<acme:textbox code="finder.keyword" path="keyWord"/>

	
	<form:label path="minDate" >
		<spring:message code="finder.minimumDate" />:
	</form:label>
	<form:input type="date"  path="minDate" format="{0,date,dd/MM/yyyy}"/>
	<br />
	
	<form:label path="maxDate">
		<spring:message code="finder.maximumDate" />:
	</form:label>
	<form:input type="date" path="maxDate" format="{0,date,dd/MM/yyyy}"/>
	<br />
	
	<acme:textbox code="finder.areaName" path="areaName"/>
	
	<acme:submit name="save" code="finder.save" />
	<br />
	
	<a href="finder/member/clean.do"><spring:message code="finder.clean" /></a>
	
	<!--  Listing grid -->

<display:table pagesize="10" class="displaytag" name="processions" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<spring:message code="procession.ticker" var="tickerHeader" />
	<display:column property="ticker" title="${tickerHeader}" sortable="false"/>

	<spring:message code="procession.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="procession.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" />
</display:table>
	
</form:form>