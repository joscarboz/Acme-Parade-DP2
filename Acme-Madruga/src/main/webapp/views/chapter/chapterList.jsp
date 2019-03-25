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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!--  Listing grid -->

<display:table pagesize="5" class="displaytag" name="chapters"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
	<spring:message code="chapter.title" var="chapterHeader" />
	<display:column property="title" title="${chapterHeader}" />

	<spring:message code="chapter.area" var="areaHeader" />
	<display:column property="area.name" title="${areaHeader}" />
>

	<!-- Actions -->

	<display:column>
		<a href="chapter/display.do?chapterId=${row.id}"> <spring:message
				code="chapter.display" />
		</a>
	</display:column>



</display:table>

<br />