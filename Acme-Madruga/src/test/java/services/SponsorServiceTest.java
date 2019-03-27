
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Sponsor;
import domain.Sponsorship;

import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private SponsorService	sponsorService;
	
	@Autowired
	private SponsorshipService	sponsorshipService;

	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un Sponsor
				"sponsor1","sponsorship1", null
			},{	//Creación incorrecta de un Sponsor falta sponsorship
				"sponsor1","", NumberFormatException.class
			},{	//Creación incorrecta de un Sponsorm falta actor
				"","sponsorship1", NumberFormatException.class
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
	protected void createAndSaveTemplate(final String name,final String SponsorshipName, final Class<?> expected) {
		Class<?> caught;
		final int sponsorId;
		Sponsor sponsor;
		final int sponsorshipId;
		Sponsorship sponsorship;
		Collection<Sponsorship> sponsorships = new ArrayList<>();

		caught = null;
		try {
			sponsorId = super.getEntityId(name);
			sponsor = this.sponsorService.findOne(sponsorId);
			sponsor.setSponsorships(sponsorships);
			this.sponsorService.save(sponsor);
			this.sponsorService.flush();
			sponsorshipId = super.getEntityId(SponsorshipName);
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			sponsorships.add(sponsorship);
			sponsor.setSponsorships(sponsorships);
			this.sponsorService.save(sponsor);
			this.sponsorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
