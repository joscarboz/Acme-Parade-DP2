
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
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FloatServiceTest extends AbstractTest {

	@Autowired
	private FloatService	floatService;


	// Covers 7.2% of the data in the project
	//Covers 2095 sentences

	protected void template(final String userName, final String title, final String desc, final int numPic, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final Float floatC = this.floatService.create();
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
			// Blank title
			{
				"brotherhood1", "", "Desc1", 1, ConstraintViolationException.class
			},
			// Blank description
			{
				"brotherhood1", "Title1", "", 1, ConstraintViolationException.class
			},
			// Creation 
			{
				"brotherhood1", "Title2", "Desc2", 1, null
			},
			// Pictures with one element
			{
				"brotherhood1", "Title3", "Desc3", 1, null
			},
			// Pictures with two elements
			{
				"brotherhood1", "Title4", "Desc4", 2, null
			},
			// Pictures with more than two elements
			{
				"brotherhood1", "Title5", "Desc5", 3, null
			},
			// Security

			// Member cannot create float
			{
				"member1", "Title6", "Desc6", 1, IllegalArgumentException.class
			},
			// Admin canot create float
			{
				"admin", "Title7", "Desc7", 1, IllegalArgumentException.class
			},
			// Anon cannot create float
			{
				null, "Title8", "Desc8", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void deleteTemplate(final String userName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);

			final int floatId = this.getEntityId("float1");
			final Float floatPop = this.floatService.findOne(floatId);
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
			// Member cannot delete Float
			{
				"member1", IllegalArgumentException.class
			},
			// Brotherhood can delete his Float 
			{
				"brotherhood1", null
			},
			// Brotherhood cant delete a float that does not belong to him
			{
				"brotherhood2", IllegalArgumentException.class
			},
			// Admin cannot delete Float
			{
				"admin", IllegalArgumentException.class
			},
			// Anon cannot delete Float
			{
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.deleteTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void updateTemplate(final String userName, final String title, final String desc, final int numPic, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final int floatId = this.getEntityId("float1");
			final Float floatU = this.floatService.findOne(floatId);
			floatU.setTitle(title);
			floatU.setDescription(desc);
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
			// Wrong update with title blank
			{
				"brotherhood1", "", "Desc1", 1, ConstraintViolationException.class
			},
			// Wrong update with desc blank
			{
				"brotherhood1", "Title1", "", 1, ConstraintViolationException.class
			},
			// float update
			{
				"brotherhood1", "Title2", "Desc2", 1, null
			},
			// Pictures with one element
			{
				"brotherhood1", "Title3", "Desc3", 1, null
			},
			// Pictures with two elements
			{
				"brotherhood1", "Title4", "Desc4", 2, null
			},
			// Pictures with more than two elements
			{
				"brotherhood1", "Title5", "Desc5", 3, null
			},
			// Security

			// Member cannot update float
			{
				"member1", "Title6", "Desc6", 1, IllegalArgumentException.class
			},
			// Admin cannot update float
			{
				"admin", "Title7", "Desc7", 1, IllegalArgumentException.class
			},
			// Anon cannot update float
			{
				null, "Title8", "Desc8", 1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.updateTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

}
