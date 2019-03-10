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

<display:table pagesize="5" class="displaytag" name="floats"
	requestURI="float/brotherhood/list.do" id="row">

	<!-- Attributes -->
	
	<spring:message code="float.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="false" />

	<spring:message code="float.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="false" />
		
	<!-- Actions -->
		
	<display:column>
		<a href="float/brotherhood/display.do?floatId=${row.id}"> <spring:message
				code="float.display" />
		</a>
	</display:column>
	
	<display:column>
		<a href="float/brotherhood/edit.do?floatId=${row.id}"> <spring:message
				code="position.edit" />
		</a>
	</display:column>
	
</display:table>
<br/>

<a href="float/brotherhood/create.do" >
	<spring:message code="float.create"/>
</a>
<br/>
