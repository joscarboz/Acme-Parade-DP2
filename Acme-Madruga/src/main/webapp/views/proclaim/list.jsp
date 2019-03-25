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

<display:table pagesize="5" class="displaytag" name="proclaims"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	
	<spring:message code="proclaim.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="false" />
		
	<!-- Actions -->
		
	<display:column>
		<a href="proclaim/${role}display.do?proclaimId=${row.id}"> <spring:message
				code="proclaim.display" />
		</a>
	</display:column>
	
</display:table>
<br/>

	<security:authorize access="hasRole('CHAPTER')">
		<a href="proclaim/chapter/create.do" >
			<spring:message code="proclaim.create"/>
		</a>
	</security:authorize>
<br/>
