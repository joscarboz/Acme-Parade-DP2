
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.MiscellaneousRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	// Covers 5.0% of the data in the project
	//Covers 1442 sentences

	@Test
	public void createAndSaveMiscellaneousRecordDriver() {
		final Object testingData[][] = {
			{	//Create miscellaneous record
				"brotherhood1", "title", "description", null
			}, {//Anon cannot create miscellaneous record
				null, "title", "description", IllegalArgumentException.class
			}, {//Member cannot create miscellaneous record
				"member1", "title", "description", IllegalArgumentException.class
			}, {//Brotherhood without inception record cannot create miscellaneous record
				"brotherhood2", "title", "description", NullPointerException.class
			}, {//Brotherhood cannot create miscellaneous record without title
				"brotherhood1", null, "description", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveMiscellaneousRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Brotherhood delete miscellaneous record
				"brotherhood1", "miscellaneousRecord1", null
			}, { //brotherhood cannot delete another one's miscellaneous record 
				"brotherhood2", "miscellaneousRecord1", IllegalArgumentException.class
			}, { //Member cannot delete miscellaneous record
				"member1", "miscellaneousRecord1", IllegalArgumentException.class
			}, { //Anon cannot delete miscellaneous record
				null, "miscellaneousRecord1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void deleteTemplate(final String userName, final String miscellaneousRecordBeanName, final Class<?> expected) {
		Class<?> caught;
		int miscellaneousRecordId;
		MiscellaneousRecord miscellaneousRecord;

		caught = null;
		try {
			this.authenticate(userName);
			miscellaneousRecordId = super.getEntityId(miscellaneousRecordBeanName);
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void createAndSaveMiscellaneousRecordTemplate(final String userName, final String title, final String description, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();
			miscellaneousRecord.setDescription(description);
			miscellaneousRecord.setTitle(title);
			this.miscellaneousRecordService.save(miscellaneousRecord);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
