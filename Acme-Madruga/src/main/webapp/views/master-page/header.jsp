<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${bannerUrl}" alt="Acme Madruga., Inc."
		height="200px" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/display.do"><spring:message
									code="master.page.profile" /></a></li>
						<li><a href="actor/edit.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MEMBER')">
						<li><a href="actor/display.do"><spring:message
									code="master.page.profile" /></a></li>
						<li><a href="actor/edit.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('BROTHERHOOD')">
						<li><a href="actor/displayBrotherhood.do"><spring:message
									code="master.page.profile" /></a></li>
						<li><a href="actor/editBrotherhood.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CHAPTER')">
						<li><a href="actor/displayChapter.do"><spring:message
									code="master.page.profile" /></a></li>
						<li><a href="actor/editChapter.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<li><a href="message/list.do?boxName=in box"><spring:message
								code="master.page.profile.messages" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="position/administrator/list.do"><spring:message
								code="master.page.administrator.position" /></a></li>
					<li><a href="area/administrator/list.do"><spring:message
								code="master.page.administrator.area" /></a></li>
					<li><a href="message/broadcast.do"><spring:message
								code="master.page.administrator.notification" /></a></li>
					<li><a href="administrator/dashboard.do"><spring:message
								code="master.page.administrator.dashboard" /></a></li>
					<li><a href="administrator/management.do"><spring:message
								code="master.page.administrator.management" /></a></li>
					<li><a href="actor/registerAdministrator.do"><spring:message
								code="master.page.administrator.register" /></a></li>
					<li><a href="administrator/getSpammers.do"><spring:message
								code="master.page.administrator.spammers" /></a></li>
					<li><a href="administrator/getScore.do"><spring:message
								code="master.page.administrator.score" /></a></li>
					<li><a href="administrator/disableSponsorships.do"><spring:message
								code="master.page.administrator.disableSponsorships" /></a></li>
					<li><a href="systemConfig/administrator/edit.do"><spring:message
								code="master.page.administrator.systemConfig" /></a></li>

				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message
						code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/member/list.do"><spring:message
								code="master.page.member.request" /></a></li>
					<li><a href="enrolment/member/list.do"><spring:message
								code="master.page.member.brotherhood" /></a></li>

					<li><a href="finder/member/edit.do"><spring:message
								code="master.page.member.finder" /></a></li>

					<li><a href="brotherhood/list.do"><spring:message
								code="master.page.brotherhood.list" /></a></li>
					<li><a href="parade/member/list.do"><spring:message
								code="master.page.member.parade" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message
						code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/brotherhood/list.do"><spring:message
								code="master.page.brotherhood.request" /></a></li>
					<li><a href="parade/brotherhood/list.do"><spring:message
								code="master.page.brotherhood.parade" /></a></li>
					<li><a href="float/brotherhood/list.do"><spring:message
								code="master.page.brotherhood.float" /></a></li>
					<li><a href="enrolment/brotherhood/list.do"><spring:message
								code="master.page.brotherhood.member" /></a></li>
					<li><a href="history/brotherhood/display.do"><spring:message
								code="master.page.brotherhood.history" /></a></li>

				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('CHAPTER')">
			<li><a class="fNiv"><spring:message
						code="master.page.chapter" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="parade/chapter/list.do"><spring:message
								code="master.page.chapter.parades" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a class="fNiv" href="actor/register.do"><spring:message
						code="master.page.register" /></a></li>
			<li><a class="fNiv" href="actor/registerBrotherhood.do"><spring:message
						code="master.page.registerBrotherhood" /></a></li>
			<li><a class="fNiv" href="actor/registerChapter.do"><spring:message
						code="master.page.registerChapter" /></a></li>
			<li><a href="brotherhood/list.do"><spring:message
						code="master.page.brotherhood.list" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a href="brotherhood/list.do"><spring:message
						code="master.page.brotherhood.list" /></a></li>
		</security:authorize>


	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

