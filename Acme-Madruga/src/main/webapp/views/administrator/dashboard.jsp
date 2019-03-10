<%--
 * dashboard.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h2>
	<spring:message code="administrator.membersPerBrotherhood" />
</h2>

<p>
	<b><spring:message code="administrator.maximum" /> :</b> ${maximummpb}
</p>
<p>
	<b><spring:message code="administrator.minimum" /> :</b> ${minimummpb}
</p>
<p>
	<b><spring:message code="administrator.average" /> :</b> ${averagempb}
</p>
<p>
	<b><spring:message code="administrator.stdev" /> :</b> ${stdevmpb}
</p>

<h2>
	<spring:message code="administrator.largestBrotherhood" />
</h2>

<jstl:forEach var="lbrotherhood" items="${largest}">

${lbrotherhood.name} ${lbrotherhood.middleName} ${lbrotherhood.surname} ${lbrotherhood.title}
<br>
</jstl:forEach>

<h2>
	<spring:message code="administrator.smallestBrotherhood" />
</h2>

<jstl:forEach var="sbrotherhood" items="${smallest}">

${sbrotherhood.name} ${sbrotherhood.middleName} ${sbrotherhood.surname} ${sbrotherhood.title}
<br>
</jstl:forEach>

<h2>
	<spring:message code="administrator.pending" />
</h2>
${pending}

<h2>
	<spring:message code="administrator.accepted" />
</h2>
${accepted}

<h2>
	<spring:message code="administrator.rejected" />
</h2>
${rejected}

<h2>
	<spring:message code="administrator.upcomingProcessions" />
</h2>

<jstl:if test="${upcomProces.isEmpty()}">
	<spring:message code="administrator.emptyupcomingProcessions" />
</jstl:if>

<jstl:forEach var="procession" items="${upcomProces}">

${procession.ticker} ${procession.title}
<br>
</jstl:forEach>

<h2>
	<spring:message code="administrator.acceptedReqMembers" />
</h2>
<jstl:forEach var="mem" items="${acceptedReqMem}">

${mem.name} ${mem.middleName} ${mem.surname}
<br>
</jstl:forEach>

<h2>
	<spring:message code="administrator.histogram" />
</h2>
<jstl:forEach var="histo" items="${histogram}">

	${histo.key.title}/${histo.key.spanishTitle} ${histo.value}
	<br>
</jstl:forEach>

<h2>
	<spring:message code="administrator.brotPerArea" />
</h2>
<jstl:forEach var="area" items="${brotpa}">

	<h4>${area.key.name}:</h4>
	<p>
		<spring:message code="administrator.ratio" />
		: ${area.value[0]}
		<spring:message code="administrator.count" />
		: ${area.value[1]}

	</p>
</jstl:forEach>

<p>
	<b><spring:message code="administrator.maximum" /> :</b> ${maximumbpa}
</p>
<p>
	<b><spring:message code="administrator.minimum" /> :</b> ${minimumbpa}
</p>
<p>
	<b><spring:message code="administrator.average" /> :</b> ${averagebpa}
</p>
<p>
	<b><spring:message code="administrator.stdev" /> :</b> ${stdevbpa}
</p>

<h2>
	<spring:message code="administrator.numPerFinder" />
</h2>

<p>
	<b><spring:message code="administrator.maximum" /> :</b> ${maximumfs}
</p>
<p>
	<b><spring:message code="administrator.minimum" /> :</b> ${minimumfs}
</p>
<p>
	<b><spring:message code="administrator.average" /> :</b> ${averagefs}
</p>
<p>
	<b><spring:message code="administrator.stdev" /> :</b> ${stdevfs}
</p>

<h2>
	<spring:message code="administrator.ratioEmpty" />
</h2>
${emptyf}
