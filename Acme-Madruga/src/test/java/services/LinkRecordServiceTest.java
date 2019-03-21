
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.LinkRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LinkRecordServiceTest extends AbstractTest {

	@Autowired
	private LinkRecordService	linkRecordService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void createAndSaveLinkRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un link record
				"brotherhood1", "title", "description", "brotherhood2", null
			}, {//Anonimo no puede crear un link record
				null, "title", "description", "brotherhood2", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear un link record
				"member1", "title", "description", "brotherhood2", IllegalArgumentException.class
			}, {//Un brotherhood sin inception record no puede tener un link record
				"brotherhood2", "title", "description", "brotherhood2", NullPointerException.class
			}, {//Un brotherhood no puede tener un link record sin brotherhood
				"brotherhood1", "title", "description", null, AssertionError.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveLinkRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void createAndSaveLinkRecordTemplate(final String userName, final String title, final String description, final String link, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final LinkRecord linkRecord = this.linkRecordService.create();
			linkRecord.setDescription(description);
			linkRecord.setTitle(title);
			final int Brotherhoodid = this.getEntityId(link);
			linkRecord.setLink(this.brotherhoodService.findOne(Brotherhoodid));
			this.linkRecordService.save(linkRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
