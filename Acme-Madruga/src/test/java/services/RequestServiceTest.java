
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


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una request
				"member2", "parade2", null
			}, {//Anonimo no puede crear una Position
				null, "parade2", IllegalArgumentException.class
			}, {//Solo member puede crear una Position
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
			{	//Un member borra correctamente una request
				"member1", "request1", null
			}, { //Un member no puede borrar la request de otro
				"member2", "request1", IllegalArgumentException.class
			}, { //Un anónimo no puede borrar una request
				null, "request1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void acceptDriver() {
		final Object testingData[][] = {
			{	//Una brotherhood acepta correctamente una request
				"brotherhood1", "request1", null
			}, { //Una brotherhood no puede aceptar la request de otro
				"brotherhood1", "request4", IllegalArgumentException.class
			}, { //Un anónimo no puede aceptar una request
				null, "request1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void rejectDriver() {
		final Object testingData[][] = {
			{	//Una brotherhood rechaza correctamente una request
				"brotherhood1", "request1", null
			}, { //Una brotherhood no puede rechazar la request de otro
				"brotherhood1", "request4", IllegalArgumentException.class
			}, { //Un anónimo no puede rechazar una request
				null, "request1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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

		System.out.println("expected: " + expected);
		System.out.println("caught: " + caught);

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
			this.requestService.acceptRequest(request);
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
			this.requestService.rejectRequest(request);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
