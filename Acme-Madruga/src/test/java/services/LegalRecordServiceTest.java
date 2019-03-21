
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.LegalRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	@Autowired
	private LegalRecordService	legalRecordService;


	@Test
	public void createAndSaveLegalRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un legal record
				"brotherhood1", "title", "description", "law", "legalName", 10, null
			}, {//Anonimo no puede crear un legal record
				null, "title", "description", "law", "legalName", 10, IllegalArgumentException.class
			}, {//Solo brotherhood puede crear un legal record
				"member1", "title", "description", "law", "legalName", 10, IllegalArgumentException.class
			}, {//Un brotherhood sin inception record no puede tener un legal record
				"brotherhood2", "title", "description", "law", "legalName", 10, NullPointerException.class
			}, {//Un brotherhood no puede tener un legal record sin legal name
				"brotherhood1", "title", "description", "law", null, 10, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveLegalRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveLegalRecordTemplate(final String userName, final String title, final String description, final String laws, final String legalName, final Integer VAT, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final LegalRecord legalRecord = this.legalRecordService.create();
			legalRecord.setDescription(description);
			legalRecord.setLaws(laws);
			legalRecord.setLegalName(legalName);
			legalRecord.setTitle(title);
			legalRecord.setVAT(VAT);
			this.legalRecordService.save(legalRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
