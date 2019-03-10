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

<display:table pagesize="5" class="displaytag" name="boxes" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<spring:message code="box.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="false" />
	
	<security:authorize access="hasRole('HANDY_WORKER')">
		<display:column>
			<a href="box/handyWorker/list.do?handyWorkerId=${handyWorker.id}">
				<spring:message	code="box.save" />
			</a>
		</display:column>
		<display:column>
			<a href="box/handyWorker/list.do?handyWorkerId=${handyWorker.id}">
				<spring:message	code="box.cancel" />
			</a>
		</display:column>	
	</security:authorize>
		
	<security:authorize access="hasRole('REFEREE')">
		<display:column>
			<a href="box/referee/list.do?refereeId=${referee.id}">
				<spring:message	code="box.save" />
			</a>
		</display:column>
		<display:column>
			<a href="box/referee/list.do?refereeId=${referee.id}">
				<spring:message	code="box.cancel" />
			</a>
		</display:column>	
	</security:authorize>	
	
	<security:authorize access="hasRole('CUSTOMER')">
		<display:column>
			<a href="box/customer/list.do?customerId=${customer.id}">
				<spring:message	code="box.save" />
			</a>
		</display:column>
		<display:column>
			<a href="box/customer/list.do?customerId=${customer.id}">
				<spring:message	code="box.cancel" />
			</a>
		</display:column>	
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="box/admin/list.do?adminId=${administrator.id}">
				<spring:message	code="box.save" />
			</a>
		</display:column>
		<display:column>
			<a href="box/admin/list.do?adminId=${administrator.id}">
				<spring:message	code="box.cancel" />
			</a>
		</display:column>	
	</security:authorize>
		
</display:table>

