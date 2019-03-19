package services;

import java.util.LinkedList;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Float;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FloatServiceTest extends AbstractTest {

	@Autowired
	private FloatService floatService;

	protected void template(final String userName, String title, String desc,
			int numPic, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			Float floatC = floatService.create();
			floatC.setTitle(title);
			floatC.setDescription(desc);
			switch (numPic) {
			case 1:
				floatC.getPictures().add("http://www.foto1.com");
				break;
			case 2:
				floatC.getPictures().add("http://www.foto1.com");
				floatC.getPictures().add("http://www.foto2.com");
				break;

			default:
				floatC.getPictures().add("http://www.foto1.com");
				floatC.getPictures().add("http://www.foto2.com");
				floatC.getPictures().add("http://www.foto3.com");
				floatC.getPictures().add("http://www.foto4.com");
				floatC.getPictures().add("http://www.foto5.com");
				floatC.getPictures().add("http://www.foto6.com");
			}

			this.floatService.save(floatC);
			this.floatService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void createAndSaveDriver() {

		final Object testingData[][] = {
				// Creación incorrecta con title blank
				{ "brotherhood1", "", "Desc1", 1,
						ConstraintViolationException.class },
				// Creación incorrecta con desc blank
				{ "brotherhood1", "Title1", "", 1,
						ConstraintViolationException.class },
				// Creación correcta de un float
				{ "brotherhood1", "Title2", "Desc2", 1, null },
				// Pictures con un sólo elemento
				{ "brotherhood1", "Title3", "Desc3", 1, null },
				// Pictures con dos elementos
				{ "brotherhood1", "Title4", "Desc4", 2, null },
				// Pictures con más de dos elementos
				{ "brotherhood1", "Title5", "Desc5", 3, null },
				// Seguridad

				// Member no puede crear un float
				{ "member1", "Title6", "Desc6", 1,
						IllegalArgumentException.class },
				// Admin no puede crear un float
				{ "admin", "Title7", "Desc7", 1, IllegalArgumentException.class },
				// Anónimo no puede crear float
				{ null, "Title8", "Desc8", 1, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(int) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void deleteTemplate(final String userName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);

			int floatId = this.getEntityId("float1");
			Float floatPop = this.floatService.findOne(floatId);
			this.floatService.delete(floatPop);
			this.floatService.flush();

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void deleteDriver() {

		final Object testingData[][] = {
				// Member no puede borrar un Float
				{ "member1", IllegalArgumentException.class },
				// Brotherhood puede borrar un Float suyo
				{ "brotherhood1", null },
				// Brotherhood no puede borrar un Float que no es suyo
				{ "brotherhood2", IllegalArgumentException.class },
				// Admin no puede borrar un Float
				{ "admin", IllegalArgumentException.class },
				// Anónimo no puede borrar un Float
				{ null, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.deleteTemplate((String) testingData[i][0],
						(Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void updateTemplate(final String userName, String title,
			String desc, int numPic, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			int floatId = this.getEntityId("float1");
			Float floatU = this.floatService.findOne(floatId);
			floatU.setTitle(title);
			floatU.setDescription(desc);
			LinkedList<String> picturesU = new LinkedList<String>();
			switch (numPic) {
			case 1:
				picturesU.add("http://www.foto1.com");
				break;
			case 2:
				picturesU.add("http://www.foto1.com");
				picturesU.add("http://www.foto2.com");
				break;

			default:
				picturesU.add("http://www.foto1.com");
				picturesU.add("http://www.foto2.com");
				picturesU.add("http://www.foto3.com");
				picturesU.add("http://www.foto4.com");
				picturesU.add("http://www.foto5.com");
				picturesU.add("http://www.foto6.com");
			}

			floatU.setPictures(picturesU);
			this.floatService.save(floatU);
			this.floatService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void updateDriver() {

		final Object testingData[][] = {
				// Actualización incorrecta con title blank
				{ "brotherhood1", "", "Desc1", 1,
						ConstraintViolationException.class },
				// Actualización incorrecta con desc blank
				{ "brotherhood1", "Title1", "", 1,
						ConstraintViolationException.class },
				// Actualización correcta de un float
				{ "brotherhood1", "Title2", "Desc2", 1, null },
				// Pictures con un sólo elemento
				{ "brotherhood1", "Title3", "Desc3", 1, null },
				// Pictures con dos elementos
				{ "brotherhood1", "Title4", "Desc4", 2, null },
				// Pictures con más de dos elementos
				{ "brotherhood1", "Title5", "Desc5", 3, null },
				// Seguridad

				// Member no puede actualizar un float
				{ "member1", "Title6", "Desc6", 1,
						IllegalArgumentException.class },
				// Admin no puede actualizar un float
				{ "admin", "Title7", "Desc7", 1, IllegalArgumentException.class },
				// Anónimo no puede actualizar un float
				{ null, "Title8", "Desc8", 1, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.updateTemplate((String) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(int) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
