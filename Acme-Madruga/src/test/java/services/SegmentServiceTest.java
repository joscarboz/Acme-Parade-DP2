
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;
import domain.Segment;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SegmentServiceTest extends AbstractTest {

	//Data coverage of 5.3%
	//Sentence coverage of 1611 sentences

	//SUT
	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private SegmentService		segmentService;


	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {

		final Object testingData[][] = {
			{	//Create correct segment
				"brotherhood1", "parade1", "segment4", "-300,-300", "-400,-400", new Date(2019, 03, 07, 9, 15), new Date(2019, 03, 07, 11, 15), null
			}, {	//Incorrect segment with origin not consecutive
				"brotherhood1", "parade1", "segment4", "-400,-400", "-500,-500", new Date(2019, 03, 07, 9, 15), new Date(2019, 03, 07, 11, 15), IllegalArgumentException.class
			}, {	//Incorrect segment with end time before start
				"brotherhood1", "parade1", "segment4", "-300,-300", "-400,-400", new Date(2019, 03, 07, 9, 15), new Date(2019, 03, 07, 5, 15), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (Date) testingData[i][6],
					(Class<?>) testingData[i][7]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String brotherhood, final String parade, final String segment, final String start, final String end, final Date startDate, final Date endDate, final Class<?> expected) {
		Class<?> caught;
		Parade par;
		Segment seg;

		Integer paradeId;

		caught = null;
		try {
			super.authenticate(brotherhood);

			paradeId = super.getEntityId(parade);
			par = this.paradeService.findOne(paradeId);
			seg = this.segmentService.create(paradeId);
			seg.setOrigin(start);
			seg.setDestination(end);
			seg.setOriginReachMoment(startDate);
			seg.setDestinationReachMoment(endDate);
			this.segmentService.save(seg, par);
			this.segmentService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
