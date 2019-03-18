
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Enrolment;
import domain.Member;
import domain.Request;

import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MemberServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private MemberService	memberService;
	
	@Autowired
	private RequestService	requestService;
	
	@Autowired
	private EnrolmentService	enrolmentService;

	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un Member
				"member1","enrolment1", "request1", null
			},{	//Creación correcta de un Member
				"member1","", "", NumberFormatException.class
			},{	//Creación correcta de un Member
				"member1","enrolment1", "", NumberFormatException.class
			},{	//Creación correcta de un Member
				"member1","", "request1", NumberFormatException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String name,final String enrolmentName, final String requestName, final Class<?> expected) {
		Class<?> caught;
		final int memberId;
		Member member;
		final int requestId;
		Request request;
		Collection<Request> requests = new ArrayList<>();
		final int enrolmentId;
		Enrolment enrolment;
		Collection<Enrolment> enrolments = new ArrayList<>();

		caught = null;
		try {
			memberId = super.getEntityId(name);
			member = this.memberService.findOne(memberId);
			member.setRequests(requests);
			member.setEnrolments(enrolments);
			this.memberService.save(member);
			this.memberService.flush();
			requestId = super.getEntityId(requestName);
			request = this.requestService.findOne(requestId);
			requests.add(request);
			enrolmentId = super.getEntityId(enrolmentName);
			enrolment = this.enrolmentService.findOne(enrolmentId);
			enrolments.add(enrolment);
			member.setRequests(requests);
			member.setEnrolments(enrolments);
			this.memberService.save(member);
			this.memberService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
