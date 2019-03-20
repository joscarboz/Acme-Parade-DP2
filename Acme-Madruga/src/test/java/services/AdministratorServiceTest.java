package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import forms.RegisterAdminForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService adminService;

	// Los test acerca de los atributos se "heredan" de los test de actor

	// Creación de users

	protected void template(final String adminName, String username,
			String password, String name, String surname, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(adminName);
			RegisterAdminForm raf = new RegisterAdminForm();
			raf.setUsername(username);
			raf.setPassword(password);
			raf.setName(name);
			raf.setSurname(surname);
			this.adminService.register(raf);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
				{ // Member no puede registrar un Admin
				"member1", "prueba", "pruebapass", "Nombre 1", "Surname 1",
						IllegalArgumentException.class },
				{ // Brotherhood no puede registrar un Admin
				"brotherhood1", "prueba", "pruebapass", "Nombre 1",
						"Surname 1", IllegalArgumentException.class },
				{ // Admin bien creado
				"admin", "prueba", "pruebapass", "Nombre 1", "Surname 1", null },
				{ // Anonimo no puede registrar un Admin
				null, "prueba", "pruebapass", "Nombre 1", "Surname 1",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(String) testingData[i][3], (String) testingData[i][4],
						(Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Dashboard

	// MembersPerBrotherhood

	protected void templateMembersPerBrotherhood(String adminName,
			Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(adminName);

			this.adminService.membersPerBrotherhoodStats();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void MembersPerBrotherhoodDriver() {
		final Object testingData[][] = { {
				// Member no puede ver dashboard
				"member1", IllegalArgumentException.class }, {
				// Brotherhood no puede ver dashborad

				"brotherhood1", IllegalArgumentException.class }, {

				// Admin puede ver dashboard
				"admin", null }, {
				// Anónimo no puede ver dashboard
				null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateMembersPerBrotherhood((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// AcceptedRequest

	protected void templateAcceptedRequest(String adminName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(adminName);

			this.adminService.acceptedRequestsRatio();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void AcceptedRequestDriver() {
		final Object testingData[][] = { {
				// Member no puede ver dashboard
				"member1", IllegalArgumentException.class }, {
				// Brotherhood no puede ver dashborad

				"brotherhood1", IllegalArgumentException.class }, {

				// Admin puede ver dashboard
				"admin", null }, {
				// Anónimo no puede ver dashboard
				null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAcceptedRequest((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// RejectedRequest

	protected void templateRejectedRequest(String adminName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(adminName);

			this.adminService.rejectedRequestsRatio();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void RejectedRequestDriver() {
		final Object testingData[][] = { {
				// Member no puede ver dashboard
				"member1", IllegalArgumentException.class }, {
				// Brotherhood no puede ver dashborad

				"brotherhood1", IllegalArgumentException.class }, {

				// Admin puede ver dashboard
				"admin", null }, {
				// Anónimo no puede ver dashboard
				null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateRejectedRequest((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// PendingRequest

	protected void templatePendingRequest(String adminName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(adminName);

			this.adminService.pendingRequestsRatio();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void PendingRequestDriver() {
		final Object testingData[][] = { {
				// Member no puede ver dashboard
				"member1", IllegalArgumentException.class }, {
				// Brotherhood no puede ver dashborad

				"brotherhood1", IllegalArgumentException.class }, {

				// Admin puede ver dashboard
				"admin", null }, {
				// Anónimo no puede ver dashboard
				null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templatePendingRequest((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// PositionHistogram

	protected void templatePositionHistogram(String adminName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(adminName);

			this.adminService.positionHistogram();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void PendingPositionHistogram() {
		final Object testingData[][] = { {
				// Member no puede ver dashboard
				"member1", IllegalArgumentException.class }, {
				// Brotherhood no puede ver dashborad

				"brotherhood1", IllegalArgumentException.class }, {

				// Admin puede ver dashboard
				"admin", null }, {
				// Anónimo no puede ver dashboard
				null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templatePositionHistogram((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
