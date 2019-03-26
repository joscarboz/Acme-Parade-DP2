
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Proclaim;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private ProclaimService	proclaimService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una Proclaim
				"chapter1", "sampleText", null
			}, {//Anonimo no puede crear una Proclaim
				null, "sampleTitle", IllegalArgumentException.class
			}, {//Solo chapter puede crear una Proclaim
				"member1", "sampleText", IllegalArgumentException.class
			}, { // Text vacío
				"chapter1", "", ConstraintViolationException.class
			}

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

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String text, final Class<?> expected) {
		Class<?> caught;
		Proclaim proclaim;

		caught = null;
		try {
			this.authenticate(userName);
			proclaim = this.proclaimService.create();
			proclaim.setText(text);
			this.proclaimService.save(proclaim);
			this.proclaimService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
