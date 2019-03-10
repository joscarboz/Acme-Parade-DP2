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

<!--  Listing grid -->

<display:table pagesize="5" class="displaytag" name="positions"
	requestURI="position/administrator/list.do" id="row">

	<!-- Attributes -->
	
	<spring:message code="position.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="false" />

	<spring:message code="position.spanishTitle" var="spanishTitleHeader" />
	<display:column property="spanishTitle" title="${spanishTitleHeader}"
		sortable="false" />
		
	<!-- Actions -->
		
	<display:column>
		<a href="position/administrator/display.do?positionId=${row.id}"> <spring:message
				code="position.display" />
		</a>
	</display:column>
	
	<display:column>
		<a href="position/administrator/edit.do?positionId=${row.id}"> <spring:message
				code="position.edit" />
		</a>
	</display:column>
	
</display:table>
<br/>

<a href="position/administrator/create.do" >
	<spring:message code="position.create"/>
</a>
<br/>
