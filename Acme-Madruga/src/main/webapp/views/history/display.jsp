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

<jstl:choose>
	<jstl:when test="${history != null}">
		<b><spring:message code="brotherhood.inceptionRecord" /></b>

		<display:table pagesize="5" class="displaytag"
			name="history.inceptionRecord" requestURI="parade/list.do" id="row">

			<!-- Attributes -->


			<spring:message code="record.title" var="titleHeader" />
			<display:column property="title" title="${titleHeader}" />

			<spring:message code="record.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" />

			<spring:message code="record.pictures" var="picturesHeader" />
			<display:column property="pictures" title="${picturesHeader}" />

			<display:column>
				<a
					href="history/inceptionRecord/edit.do?inceptionRecordId=${row.id}">
					<spring:message code="record.edit" />
				</a>
			</display:column>




		</display:table>

		<b><spring:message code="brotherhood.legalRecords" /></b>

		<display:table pagesize="5" class="displaytag"
			name="history.legalRecords" requestURI="parade/list.do" id="row">

			<!-- Attributes -->


			<spring:message code="record.title" var="titleHeader" />
			<display:column property="title" title="${titleHeader}" />

			<spring:message code="record.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" />

			<spring:message code="record.laws" var="lawsHeader" />
			<display:column property="laws" title="${lawsHeader}" />

			<spring:message code="record.legalName" var="legalNameHeader" />
			<display:column property="legalName" title="${legalNameHeader}" />

			<spring:message code="record.VAT" var="VATHeader" />
			<display:column property="VAT" title="${VATHeader}" />

			<display:column>
				<a href="history/legalRecord/edit.do?legalRecordId=${row.id}"> <spring:message
						code="record.edit" />
				</a>
			</display:column>

			<display:column>
				<a href="history/legalRecord/delete.do?legalRecordId=${row.id}">
					<spring:message code="record.delete" />
				</a>
			</display:column>

		</display:table>
		<a href="history/legalRecord/create.do"> <spring:message
				code="record.create" />
		</a>
		<br />


		<b><spring:message code="brotherhood.linkRecords" /></b>
		<display:table pagesize="5" class="displaytag"
			name="history.linkRecords" requestURI="parade/list.do" id="row">

			<!-- Attributes -->


			<spring:message code="record.title" var="titleHeader" />
			<display:column property="title" title="${titleHeader}" />

			<spring:message code="record.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" />

			<spring:message code="record.link" var="linkHeader" />
			<display:column title="${linkHeader}">
				<a href="brotherhood/display.do?brotherhoodId=${row.link.id}">${row.link.title}</a>
			</display:column>

			<display:column>
				<a href="history/linkRecord/edit.do?linkRecordId=${row.id}"> <spring:message
						code="record.edit" />
				</a>
			</display:column>

			<display:column>
				<a href="history/linkRecord/delete.do?linkRecordId=${row.id}"> <spring:message
						code="record.delete" />
				</a>
			</display:column>

		</display:table>
		<a href="history/linkRecord/create.do"> <spring:message
				code="record.create" />
		</a>
		<br />

		<b><spring:message code="brotherhood.miscellaneousRecords" /></b>
		<display:table pagesize="5" class="displaytag"
			name="history.miscellaneousRecords" requestURI="parade/list.do"
			id="row">

			<!-- Attributes -->


			<spring:message code="record.title" var="titleHeader" />
			<display:column property="title" title="${titleHeader}" />

			<spring:message code="record.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" />

			<display:column>
				<a
					href="history/miscellaneousRecord/edit.do?miscellaneousRecordId=${row.id}">
					<spring:message code="record.edit" />
				</a>
			</display:column>

			<display:column>
				<a
					href="history/miscellaneousRecord/delete.do?miscellaneousRecordId=${row.id}">
					<spring:message code="record.delete" />
				</a>
			</display:column>


		</display:table>
		<a href="history/miscellaneousRecord/create.do"> <spring:message
				code="record.create" />
		</a>
		<br />


		<b><spring:message code="brotherhood.periodRecords" /></b>
		<display:table pagesize="5" class="displaytag"
			name="history.periodRecords" requestURI="parade/list.do" id="row">

			<!-- Attributes -->


			<spring:message code="record.title" var="titleHeader" />
			<display:column property="title" title="${titleHeader}" />

			<spring:message code="record.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" />

			<spring:message code="record.pictures" var="picturesHeader" />
			<display:column property="pictures" title="${picturesHeader}" />

			<spring:message code="record.startYear" var="startYearHeader" />
			<display:column property="startYear" title="${startYearHeader}" />

			<spring:message code="record.endYear" var="endYearHeader" />
			<display:column property="endYear" title="${endYearHeader}" />

			<display:column>
				<a href="history/periodRecord/edit.do?periodRecordId=${row.id}">
					<spring:message code="record.edit" />
				</a>
			</display:column>

			<display:column>
				<a href="history/periodRecord/delete.do?periodRecordId=${row.id}">
					<spring:message code="record.delete" />
				</a>
			</display:column>

		</display:table>
		<a href="history/periodRecord/create.do"> <spring:message
				code="record.create" />
		</a>
		<br />
	</jstl:when>
	<jstl:otherwise>
		<a href="history/inceptionRecord/create.do"> <spring:message
				code="record.createInception" />
		</a>

	</jstl:otherwise>
</jstl:choose>

