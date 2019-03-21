
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


	@Test
	public void createAndSaveMiscellaneousRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un miscellaneous record
				"brotherhood1", "title", "description", null
			}, {//Anonimo no puede crear un miscellaneous record
				null, "title", "description", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear un miscellaneous record
				"member1", "title", "description", IllegalArgumentException.class
			}, {//Un brotherhood sin inception record no puede tener un miscellaneous record
				"brotherhood2", "title", "description", NullPointerException.class
			}, {//Un brotherhood no puede tener un miscellaneous record sin title
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

	protected void createAndSaveMiscellaneousRecordTemplate(final String userName, final String title, final String description, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();
			miscellaneousRecord.setDescription(description);
			miscellaneousRecord.setTitle(title);
			this.miscellaneousRecordService.save(miscellaneousRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
