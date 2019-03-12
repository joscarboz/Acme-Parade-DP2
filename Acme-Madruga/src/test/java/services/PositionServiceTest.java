
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Position;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private PositionService	positionService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una Position
				"admin", "sampleTitle", "sampleSpanishTitle", null
			}, {//Anonimo no puede crear una Position
				null, "sampleTitle", "sampleSpanishTitle", IllegalArgumentException.class
			}, {//Solo admin puede crear una Position
				"member1", "sampleTitle", "sampleSpanishTitle", IllegalArgumentException.class
			}, { // Title vacío
				"admin", "", "sampleSpanishTitle", ConstraintViolationException.class
			}, { // Spanish Title vacío
				"admin", "sampleTitle", "", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Un admin borra correctamente una position
				"admin", "position2", null
			}, { //Un admin no puede borrar una position que está en uso
				"admin", "position1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String title, final String spanishTitle, final Class<?> expected) {
		Class<?> caught;
		Position position;

		caught = null;
		try {
			this.authenticate(userName);
			position = this.positionService.create();
			position.setTitle(title);
			position.setSpanishTitle(spanishTitle);
			this.positionService.save(position);
			this.positionService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String positionBeanName, final Class<?> expected) {
		Class<?> caught;
		int positionId;
		Position position;

		caught = null;
		try {
			this.authenticate(userName);
			positionId = super.getEntityId(positionBeanName);
			position = this.positionService.findOne(positionId);
			this.positionService.delete(position);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
