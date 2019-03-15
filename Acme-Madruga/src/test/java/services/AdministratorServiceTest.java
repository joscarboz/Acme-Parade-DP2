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

	// Problema: Could not extract ResultSet

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
				{ // Member no puede crear un Finder
				"member1", "prueba", "pruebapass", "Nombre 1", "Surname 1",
						IllegalArgumentException.class },
				{ // Brotherhood no puede crear un Finder
				"brotherhood1", "prueba", "pruebapass", "Nombre 1",
						"Surname 1", IllegalArgumentException.class },
				{ // Admin bien creado
				"admin", "prueba", "pruebapass", "Nombre 1", "Surname 1", null },
				{ // Anonimo no puede crear un Admin
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

}
