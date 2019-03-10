
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.SystemConfig;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private SystemConfigService	systemConfigService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{ //Guardado Correcto del SystemConfig
				"admin", "https://tinyurl.com/acme-parada", 12.0, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", null
			}, { //URL del banner invalida
				"admin", "No link", 12.0, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//Horas de cache debajo del rango
				"admin", "https://tinyurl.com/acme-parada", 0.5, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, { //Horas de cache en por encima del rango
				"admin", "https://tinyurl.com/acme-parada", 25.0, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, { //Resultados del finder por debajo del rango
				"admin", "https://tinyurl.com/acme-parada", 12.0, 0, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//Resultados del finder por encima del rango
				"admin", "https://tinyurl.com/acme-parada", 12.0, 150, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//Nombre en blanco
				"admin", "https://tinyurl.com/acme-parada", 12.0, 50, "", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//URL en blanco
				"admin", "", 12.0, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//Mensaje de bienvenida español en blanco
				"admin", "https://tinyurl.com/acme-parada", 12.0, 50, "New name", "", "Welcome to Acme Parade", "+55", ConstraintViolationException.class
			}, {//Mensaje de bienvenida ingles en blanco
				"admin", "https://tinyurl.com/acme-parada", 12.0, 50, "New name", "Bienvenido a Acme Parade", "", "+55", ConstraintViolationException.class
			}, {//Prefijo Telefonico en blanco
				"admin", "https://tinyurl.com/acme-parada", 12.0, 50, "New name", "Bienvenido a Acme Parade", "Welcome to Acme Parade", "", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (Double) testingData[i][2], (int) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String banner, final Double finderCacheHours, final int finderMaxResults, final String name, final String welcomeMessageEsp, final String welcomeMessageEng, final String phonePrefix,
		final Class<?> expected) {
		Class<?> caught;
		SystemConfig systemConfig;

		caught = null;
		try {
			this.authenticate(userName);
			systemConfig = this.systemConfigService.findSystemConfiguration();
			systemConfig.setBanner(banner);
			systemConfig.setFinderCacheHours(finderCacheHours);
			systemConfig.setFinderMaxResults(finderMaxResults);
			systemConfig.setName(name);
			systemConfig.setWelcomeMessageEsp(welcomeMessageEsp);
			systemConfig.setWelcomeMessageEng(welcomeMessageEng);
			systemConfig.setPhonePrefix(phonePrefix);
			this.systemConfigService.save(systemConfig);
			this.systemConfigService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
