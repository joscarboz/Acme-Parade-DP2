
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Parade;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	//Data coverage of 5.0%
	//Sentence coverage of 1514 sentences

	//SUT
	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private CreditCardService	creditCardService;


	// Tests ------------------------------------------------------------------
	//Data coverage of 4.9%
	//Sentence coverage of 1506 sentences

	@Test
	public void createAndSaveDriver() {

		final Object testingData[][] = {
			{	//Create correct sponsorship
				"sponsor1", "parade2", "sponsorship5", "https://www.banner.com", "https://www.coooo.com", 18.2, true, null
			}, {	//Incorrect sponsorship with invalidad banner url
				"sponsor1", "parade2", "sponsorship5", "https://www.banner.com", "hm", 18.2, true, ConstraintViolationException.class
			}, {	//Incorrect sponsorship with invalid targetUrl
				"sponsor1", "parade2", "sponsorship5", "htss", "https://www.coooo.com", 18.2, true, ConstraintViolationException.class
			}, {	//Incorrect sponsorship with negative fare
				"sponsor1", "parade2", "sponsorship5", "https://www.cooo.com", "https://www.coooo.com", -18.2, true, ConstraintViolationException.class
			}, {	//Member cannot create sponsorship
				"member", "parade2", "sponsorship5", "https://www.cooo.com", "https://www.coooo.com", 18.2, true, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Double) testingData[i][5], (Boolean) testingData[i][6],
					(Class<?>) testingData[i][7]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String sponsor, final String parade, final String sponsorship, final String banner, final String target, final Double fare, final Boolean active, final Class<?> expected) {
		Class<?> caught;
		Sponsorship spon;
		Parade par;
		CreditCard cc;

		Integer paradeId;

		caught = null;
		try {
			super.authenticate(sponsor);
			paradeId = super.getEntityId(parade);

			cc = this.creditCardService.create();
			cc.setCvv(234);
			cc.setExpirationMonth(11);
			cc.setExpirationYear(21);
			cc.setHolder("Holder");
			cc.setMake("VISA");
			cc.setNumber("4279891548192283");

			par = this.paradeService.findOne(paradeId);
			spon = this.sponsorshipService.create();
			spon.setActive(active);
			spon.setBanner(banner);
			spon.setCreditCard(cc);
			spon.setFare(fare);
			spon.setParade(par);
			spon.setTargetUrl(target);
			this.sponsorshipService.save(spon, cc);
			this.sponsorshipService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
