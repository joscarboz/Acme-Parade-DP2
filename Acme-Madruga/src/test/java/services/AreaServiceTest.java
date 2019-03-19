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
import domain.Area;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class AreaServiceTest extends AbstractTest {

	@Autowired
	private AreaService areaService;

	protected void template(final String userName, String name, int numPic,
			Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			Area areaC = areaService.create();
			areaC.setName(name);
			switch (numPic) {
			case 1:
				areaC.getPictures().add("http://www.foto1.com");
				break;
			case 2:
				areaC.getPictures().add("http://www.foto1.com");
				areaC.getPictures().add("http://www.foto2.com");
				break;

			default:
				areaC.getPictures().add("http://www.foto1.com");
				areaC.getPictures().add("http://www.foto2.com");
				areaC.getPictures().add("http://www.foto3.com");
				areaC.getPictures().add("http://www.foto4.com");
				areaC.getPictures().add("http://www.foto5.com");
				areaC.getPictures().add("http://www.foto6.com");
			}

			this.areaService.save(areaC);
			this.areaService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void createAndSaveDriver() {

		final Object testingData[][] = {
				// Creación incorrecta con name blank
				{ "admin", "", 1, ConstraintViolationException.class },
				// Creación correcta de un area (name correcto)
				{ "admin", "Name 1", 1, null },
				// Pictures con un sólo elemento
				{ "admin", "Name 2", 1, null },
				// Pictures con dos elementos
				{ "admin", "Name 3", 2, null },
				// Pictures con más de dos elementos
				{ "admin", "Name 4", 3, null },
				// Seguridad

				// Member no puede crear un area
				{ "member1", "Name 5", 1, IllegalArgumentException.class },
				// Brotherhood no puede crear un area
				{ "brotherhood1", "Name 6", 1, IllegalArgumentException.class },
				// Anónimo no puede crear area
				{ null, "Name 7", 1, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0],
						(String) testingData[i][1], (int) testingData[i][2],
						(Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void deleteTemplate(final String userName, boolean area,
			Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			if (area) {

				int areaId = this.getEntityId("area1");
				Area areaPop = this.areaService.findOne(areaId);
				this.areaService.delete(areaPop);
				this.areaService.flush();
			} else {
				Area areaC = this.areaService.create();
				areaC.setName("Area sin nada");
				Area areaSinNada = this.areaService.save(areaC);
				this.areaService.delete(areaSinNada);
				this.areaService.flush();
			}

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void deleteDriver() {

		final Object testingData[][] = {
				// Member no puede borrar un Area
				{ "member1", true, IllegalArgumentException.class },
				// Brotherhood no puede borrar un Area
				{ "brotherhood1", true, IllegalArgumentException.class },
				// Admin no puede borrar un area porque tiene brotherhood
				// asociado.
				{ "admin", true, IllegalArgumentException.class },
				// Anónimo no puede borrar un Area
				{ null, true, IllegalArgumentException.class },
				// Admin puede borrar el area porque no hay ningún brotherhood
				// asociado a ese area
				{ "admin", false, null } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.deleteTemplate((String) testingData[i][0],
						(boolean) testingData[i][1],
						(Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void updateTemplate(final String userName, String name,
			int numPic, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			int areaId = this.getEntityId("area1");
			Area areaU = this.areaService.findOne(areaId);
			areaU.setName(name);
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

			areaU.setPictures(picturesU);
			this.areaService.save(areaU);
			this.areaService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void updateDriver() {

		final Object testingData[][] = {
				// Actualización incorrecta con name blank
				{ "admin", "", 1, ConstraintViolationException.class },
				// Actualización correcta de un area (name correcto)
				{ "admin", "Name 1", 1, null },
				// Actualización Pictures con un sólo elemento
				{ "admin", "Name 2", 1, null },
				// Actualización Pictures con dos elementos
				{ "admin", "Name 3", 2, null },
				// Actualización Pictures con más de dos elementos
				{ "admin", "Name 4", 3, null },
				// Seguridad

				// Member no puede actualizar un area
				{ "member1", "Name 5", 1, IllegalArgumentException.class },
				// Brotherhood no puede actualizar un area
				{ "brotherhood1", "Name 6", 1, IllegalArgumentException.class },
				// Anónimo no puede actualizar area
				{ null, "Name 7", 1, IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.updateTemplate((String) testingData[i][0],
						(String) testingData[i][1], (int) testingData[i][2],
						(Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
