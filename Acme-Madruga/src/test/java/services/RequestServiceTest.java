
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Request;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RequestServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private RequestService	requestService;


	// Data coverage of 8.7%
	// Sentence coverage of 2519 sentences

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Create request
				"member2", "parade2", null
			}, {//Anon cannot create request
				null, "parade2", IllegalArgumentException.class
			}, {//Only member can create a request 
				"brotherhood1", "parade2", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Member deletes request
				"member1", "request1", null
			}, { //Member cannot delete another one's request
				"member2", "request1", IllegalArgumentException.class
			}, { //Anon cannot create request
				null, "request1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void acceptDriver() {
		final Object testingData[][] = {
			{	//Brotherhood accepts request
				"brotherhood1", "request1", null
			}, { //Brotherhood cannot accept another one's request
				"brotherhood1", "request4", IllegalArgumentException.class
			}, { //Anon cannot accept request
				null, "request1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void rejectDriver() {
		final Object testingData[][] = {
			{	//Brotherhood rejects request
				"brotherhood2", "request4", null
			}, { //Brotherhood cannot reject another one's request
				"brotherhood1", "request4", IllegalArgumentException.class
			}, { //Anon cannot reject request
				null, "request4", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.rejectTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		final Request request;

		caught = null;
		try {
			this.authenticate(userName);
			final Integer paradeId = super.getEntityId(paradeBeanName);
			request = this.requestService.registerPrincipal(paradeId);
			final Request saved = this.requestService.save(request);
			this.requestService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	protected void deleteTemplate(final String userName, final String requestBeanName, final Class<?> expected) {
		Class<?> caught;
		final int requestId;
		Request request;

		caught = null;
		try {
			this.authenticate(userName);
			requestId = super.getEntityId(requestBeanName);
			request = this.requestService.findOne(requestId);
			this.requestService.delete(request);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void acceptTemplate(final String userName, final String requestBeanName, final Class<?> expected) {
		Class<?> caught;
		final int requestId;
		Request request;

		caught = null;
		try {
			this.authenticate(userName);
			requestId = super.getEntityId(requestBeanName);
			request = this.requestService.findOne(requestId);
			final Request accepted = this.requestService.acceptRequest(request);
			this.requestService.save(accepted);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void rejectTemplate(final String userName, final String requestBeanName, final Class<?> expected) {
		Class<?> caught;
		final int requestId;
		Request request;

		caught = null;
		try {
			this.authenticate(userName);
			requestId = super.getEntityId(requestBeanName);
			request = this.requestService.findOne(requestId);
			final Request rejected = this.requestService.rejectRequest(request);
			this.requestService.save(rejected);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
