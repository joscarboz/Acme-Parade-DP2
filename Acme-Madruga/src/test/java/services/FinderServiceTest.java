
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private FinderService	finderService;

	Calendar				calendario	= new GregorianCalendar(2019, 03, 10);
	Date					fechaMinima	= this.calendario.getTime();

	Calendar				calendario2	= new GregorianCalendar(2019, 03, 14);
	Date					fechaMáxima	= this.calendario2.getTime();


	// Tests ------------------------------------------------------------------
	// Covers 4.8% of the data in the project
	//Covers 1387 sentences

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	// Finder creation
				"member1", "sampleKeyword", this.fechaMinima, this.fechaMáxima, "sampleArea", null
			}, { //Brotherhood cannot create Finder
				"brotherhood1", "sampleKeyword", this.fechaMinima, this.fechaMáxima, "sampleArea", IllegalArgumentException.class
			}, { //Admin cannot create Finder
				"admin", "sampleKeyword", this.fechaMinima, this.fechaMáxima, "sampleArea", IllegalArgumentException.class
			}, { // Anonimo cannot create Finder
				null, "", this.fechaMinima, this.fechaMáxima, "sampleArea", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String keyword, final Date minDate, final Date maxDate, final String areaName, final Class<?> expected) {
		Class<?> caught;
		Finder finder;

		caught = null;
		try {
			this.authenticate(userName);
			finder = this.finderService.create();
			finder.setKeyWord(keyword);
			finder.setMinDate(minDate);
			finder.setMaxDate(maxDate);
			finder.setAreaName(areaName);
			this.finderService.save(finder);
			this.finderService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
