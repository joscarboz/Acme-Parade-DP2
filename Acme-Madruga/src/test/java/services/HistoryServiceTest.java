
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HistoryServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private InceptionRecordService		inceptionRecordService;
	@Autowired
	private LegalRecordService			legalRecordService;
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	private LinkRecordService			linkRecordService;
	@Autowired
	private PeriodRecordService			periodRecordService;
	@Autowired
	private BrotherhoodService			brotherhoodService;


	@Test
	public void createAndSaveInceptionRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una history con su inception record
				"brotherhood2", null
			}, {//Anonimo no puede crear una history con su inception record
				null, IllegalArgumentException.class
			}, {//Solo brotherhood puede crear una history con su inception record
				"member1", IllegalArgumentException.class
			}, {//Un brotherhood solo puede tener una history con su inception record
				"brotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveInceptionRecordTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void createAndSavePeriodRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un period record
				"brotherhood1", "title", "description", 1939, 1949, "https://www.photo.com", null
			}, {//Anonimo no puede crear un period record
				null, "title", "description", 1939, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear un period record
				"member1", "title", "description", 1939, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//El end year no puede ser anterior al start year
				"brotherhood1", "title", "description", 1959, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//El title no puede ser nulo
				"brotherhood1", null, "description", 1939, 1949, "https://www.photo.com", ConstraintViolationException.class
			}, {//El end year no puede ser negativo
				"brotherhood1", "title", "description", 1939, -1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//Una brotherhood sin inception record no puede tener un period record
				"brotherhood2", "title", "description", 1939, 1949, "https://www.photo.com", NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSavePeriodRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

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

	protected void createAndSavePeriodRecordTemplate(final String userName, final String title, final String description, final Integer startYear, final Integer endYear, final String picture, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final PeriodRecord periodRecord = this.periodRecordService.create();
			periodRecord.setDescription(description);
			periodRecord.setEndYear(endYear);
			periodRecord.setStartYear(startYear);
			periodRecord.setTitle(title);
			final List<String> pictures = new ArrayList<String>();
			pictures.add(picture);
			final Collection<String> photos = pictures;
			periodRecord.setPictures(photos);
			this.periodRecordService.save(periodRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
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

	protected void createAndSaveInceptionRecordTemplate(final String userName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
			inceptionRecord.setDescription("description");
			inceptionRecord.setTitle("title");
			final String photo1 = "https://www.photo.com";
			final List<String> pictures = new ArrayList<String>();
			pictures.add(photo1);
			final Collection<String> photos = pictures;
			inceptionRecord.setPictures(photos);
			this.inceptionRecordService.save(inceptionRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
