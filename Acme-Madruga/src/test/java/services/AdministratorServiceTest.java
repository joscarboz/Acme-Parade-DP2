
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
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	adminService;


	// Covers 6.7% of the data in the project
	//Covers 1934 sentences

	protected void template(final String adminName, final String username, final String password, final String name, final String surname, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(adminName);
			final RegisterAdminForm raf = new RegisterAdminForm();
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
			{ // Member can't register an admin
				"member1", "prueba", "pruebapass", "Nombre 1", "Surname 1", IllegalArgumentException.class
			}, { // Brotherhood can't register an Admin
				"brotherhood1", "prueba", "pruebapass", "Nombre 1", "Surname 1", IllegalArgumentException.class
			}, { // Admin created
				"admin", "prueba", "pruebapass", "Nombre 1", "Surname 1", null
			}, { // Anon can't register an admin
				null, "prueba", "pruebapass", "Nombre 1", "Surname 1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Dashboard

	// MembersPerBrotherhood

	protected void templateMembersPerBrotherhood(final String adminName, final Class<?> expected) {
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
		final Object testingData[][] = {
			{
				// Member can't displau dashboard
				"member1", IllegalArgumentException.class
			}, {
				// Brotherhood can't display dashborad

				"brotherhood1", IllegalArgumentException.class
			}, {

				// Admin can display dashboard
				"admin", null
			}, {
				// Anónimo can't display dashboard
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateMembersPerBrotherhood((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// AcceptedRequest

	protected void templateAcceptedRequest(final String adminName, final Class<?> expected) {
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
		final Object testingData[][] = {
			{
				// Member can't display dashboard
				"member1", IllegalArgumentException.class
			}, {
				// Brotherhood can't display dashborad

				"brotherhood1", IllegalArgumentException.class
			}, {

				// Admin can display dashboard
				"admin", null
			}, {
				// Anon can't display dashboard
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAcceptedRequest((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// RejectedRequest

	protected void templateRejectedRequest(final String adminName, final Class<?> expected) {
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
		final Object testingData[][] = {
			{
				// Member cannot display dashboard
				"member1", IllegalArgumentException.class
			}, {
				// Brotherhood cannot display dashborad

				"brotherhood1", IllegalArgumentException.class
			}, {

				// Admin can display dashboard
				"admin", null
			}, {
				// Anon cannot display dashboard
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateRejectedRequest((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// PendingRequest

	protected void templatePendingRequest(final String adminName, final Class<?> expected) {
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
		final Object testingData[][] = {
			{
				// Member cannot display dashboard
				"member1", IllegalArgumentException.class
			}, {
				// Brotherhood cannot display dashborad

				"brotherhood1", IllegalArgumentException.class
			}, {

				// Admin can display dashboard
				"admin", null
			}, {
				// Anon cannot display dashboard
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templatePendingRequest((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// PositionHistogram

	protected void templatePositionHistogram(final String adminName, final Class<?> expected) {
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
		final Object testingData[][] = {
			{
				// Member cannot display dashboard
				"member1", IllegalArgumentException.class
			}, {
				// Brotherhood cannot display dashborad

				"brotherhood1", IllegalArgumentException.class
			}, {

				// Admin can display dashboard
				"admin", null
			}, {
				// Anon cannot display dashboard
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templatePositionHistogram((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
