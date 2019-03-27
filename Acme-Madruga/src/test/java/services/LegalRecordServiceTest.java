
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


	// Covers 5.4% of the data in the project
	//Covers 1561 sentences

	@Test
	public void createAndSaveLegalRecordDriver() {
		final Object testingData[][] = {
			{	//Legal record create
				"brotherhood1", "title", "description", "law", "legalName", 10, null
			}, {//Anon cannot create legal record
				null, "title", "description", "law", "legalName", 10, IllegalArgumentException.class
			}, {//Only brotherhood can create legal record
				"member1", "title", "description", "law", "legalName", 10, IllegalArgumentException.class
			}, {//Brotherhood without inception record cannot create legal record
				"brotherhood2", "title", "description", "law", "legalName", 10, NullPointerException.class
			}, {//Brotherhood cannot create legal record without legal name
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

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Legal record delete
				"brotherhood1", "legalRecord1", null
			}, { //Brotherhood cannot delete another one's legal record
				"brotherhood2", "legalRecord1", IllegalArgumentException.class
			}, { //Only brotherhood can delete legal record
				"member1", "legalRecord1", IllegalArgumentException.class
			}, { //Anon cannot delete legal record
				null, "legalRecord1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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
			this.legalRecordService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String legalRecordBeanName, final Class<?> expected) {
		Class<?> caught;
		int legalRecordId;
		LegalRecord legalRecord;

		caught = null;
		try {
			this.authenticate(userName);
			legalRecordId = super.getEntityId(legalRecordBeanName);
			legalRecord = this.legalRecordService.findOne(legalRecordId);
			this.legalRecordService.delete(legalRecord);
			this.legalRecordService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
