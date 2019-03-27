
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


	// Data coverage of 3.7%
	// Sentence coverage of 1078 sentences

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Create proclaim
				"chapter1", "sampleText", null
			}, {//Anonimo cannot create Proclaim
				null, "sampleTitle", IllegalArgumentException.class
			}, {//Only chapter can create Proclaim
				"member1", "sampleText", IllegalArgumentException.class
			}, { // Empty text
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
