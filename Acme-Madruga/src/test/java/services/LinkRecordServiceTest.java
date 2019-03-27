
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


	// Covers 5.2% of the data in the project
	//Covers 1500 sentences

	@Test
	public void createAndSaveLinkRecordDriver() {
		final Object testingData[][] = {
			{	//Create link record
				"brotherhood1", "title", "description", "brotherhood2", null
			}, {//Anon cannot create link record
				null, "title", "description", "brotherhood2", IllegalArgumentException.class
			}, {//Member cannot create link record
				"member1", "title", "description", "brotherhood2", IllegalArgumentException.class
			}, {//Brotherhood without inception record cannot create link record
				"brotherhood2", "title", "description", "brotherhood2", NullPointerException.class
			}, {//Brotherhood cannot create link record without link
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

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Brotherhood delete link record
				"brotherhood1", "linkRecord1", null
			}, { //Brotherhood cannot delete another one's link record
				"brotherhood2", "linkRecord1", IllegalArgumentException.class
			}, { //Member cannot delete link record
				"member1", "linkRecord1", IllegalArgumentException.class
			}, { //Anon cannot delete link record
				null, "linkRecord1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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
			this.linkRecordService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String linkRecordBeanName, final Class<?> expected) {
		Class<?> caught;
		int linkRecordId;
		LinkRecord linkRecord;

		caught = null;
		try {
			this.authenticate(userName);
			linkRecordId = super.getEntityId(linkRecordBeanName);
			linkRecord = this.linkRecordService.findOne(linkRecordId);
			this.linkRecordService.delete(linkRecord);
			this.linkRecordService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
