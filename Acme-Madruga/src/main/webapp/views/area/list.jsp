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

<display:table pagesize="5" class="displaytag" name="areas"
	requestURI="area/administrator/list.do" id="row">

	<!-- Attributes -->
	
	<spring:message code="area.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}"
		sortable="true" />
		
	<!-- Actions -->
	
	<display:column>
		<a href="area/administrator/edit.do?areaId=${row.id}"> <spring:message
				code="area.edit" />
		</a>
	</display:column>
	
</display:table>
<br/>

<a href="area/administrator/create.do" >
	<spring:message code="area.create"/>
</a>
<br/>