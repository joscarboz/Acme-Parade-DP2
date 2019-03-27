
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
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AreaServiceTest extends AbstractTest {

	@Autowired
	private AreaService	areaService;


	// Covers 6.3% of the data in the project
	//Covers 1824 sentences

	protected void template(final String userName, final String name, final int numPic, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final Area areaC = this.areaService.create();
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
			// Cannot create area with blank name
			{
				"admin", "", 1, ConstraintViolationException.class
			},
			// Correct creation
			{
				"admin", "Name 1", 1, null
			},
			// Pictures with just an element
			{
				"admin", "Name 2", 1, null
			},
			// Pictures with two elements
			{
				"admin", "Name 3", 2, null
			},
			// Pictures with more than thre elements
			{
				"admin", "Name 4", 3, null
			},
			// Seguridad

			// Member cannot create area
			{
				"member1", "Name 5", 1, IllegalArgumentException.class
			},
			// Brotherhood cannot create area
			{
				"brotherhood1", "Name 6", 1, IllegalArgumentException.class
			},
			// Anónimo cannot create area
			{
				null, "Name 7", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void deleteTemplate(final String userName, final boolean area, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			if (area) {

				final int areaId = this.getEntityId("area1");
				final Area areaPop = this.areaService.findOne(areaId);
				this.areaService.delete(areaPop);
				this.areaService.flush();
			} else {
				final Area areaC = this.areaService.create();
				areaC.setName("Area sin nada");
				final Area areaSinNada = this.areaService.save(areaC);
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
			// Member cannot delete Area
			{
				"member1", true, IllegalArgumentException.class
			},
			// Brotherhood cannot delete Area
			{
				"brotherhood1", true, IllegalArgumentException.class
			},
			// Admin cannot delete area with brotherhood
			// asociated
			{
				"admin", true, IllegalArgumentException.class
			},
			// Anon cannot create Area
			{
				null, true, IllegalArgumentException.class
			},
			// Admin can delete area with no brotherhood
			{
				"admin", false, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.deleteTemplate((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void updateTemplate(final String userName, final String name, final int numPic, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final int areaId = this.getEntityId("area1");
			final Area areaU = this.areaService.findOne(areaId);
			areaU.setName(name);
			final LinkedList<String> picturesU = new LinkedList<String>();
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
			// Wrong update with blank name
			{
				"admin", "", 1, ConstraintViolationException.class
			},
			// Right update
			{
				"admin", "Name 1", 1, null
			},
			// Right update with one Picture
			{
				"admin", "Name 2", 1, null
			},
			// Right update with two Pictures
			{
				"admin", "Name 3", 2, null
			},
			// Right update with more than three Pictures
			{
				"admin", "Name 4", 3, null
			},
			// Seguridad

			// Member cannot update area
			{
				"member1", "Name 5", 1, IllegalArgumentException.class
			},
			// Brotherhood cannot update area
			{
				"brotherhood1", "Name 6", 1, IllegalArgumentException.class
			},
			// Anon cannot update area
			{
				null, "Name 7", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.updateTemplate((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
