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

<form:form action="systemConfig/administrator/edit.do"
	modelAttribute="systemConfigForm">

	
	<acme:textbox code="systemConfig.name" path="name"/>
	<br />
	
	<acme:textbox code="systemConfig.banner" path="banner"/>
	<br />
	
	<acme:textbox code="systemConfig.welcomeMessageEng" path="welcomeMessageEng"/>
	<br />
	
	<acme:textbox code="systemConfig.welcomeMessageEsp" path="welcomeMessageEsp"/>
	<br />
	
	<acme:textbox code="systemConfig.phonePrefix" path="phonePrefix"/>
	<br />
	
	<acme:textbox code="systemConfig.positiveWords" path="positiveWords"/>
	<br />
	
	<acme:textbox code="systemConfig.negativeWords" path="negativeWords"/>
	<br />
	
	<acme:textbox code="systemConfig.spamWords" path="spamWords"/>
	<br />
	
	<acme:textbox code="systemConfig.finderMaxResults" path="finderMaxResults"/>
	<br />
	
	<acme:textbox code="systemConfig.finderCacheHours" path="finderCacheHours"/>
	<br />
	
	
	<acme:submit name="save" code="systemConfig.save" />
	

</form:form>