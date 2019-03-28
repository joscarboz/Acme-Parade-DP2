
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private SystemConfigService		systemConfigService;


	public Sponsorship create() {
		Sponsorship result;

		result = new Sponsorship();
		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAll();

		return result;
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		this.sponsorshipRepository.delete(sponsorship);
	}

	public Sponsorship save(final Sponsorship sponsorship, final CreditCard creditCard) {
		Sponsorship result;
		Sponsor sponsor;
		Assert.notNull(sponsorship);
		Assert.notNull(creditCard);

		final CreditCard savedCreditCard = this.creditCardService.save(creditCard);
		sponsorship.setCreditCard(savedCreditCard);
		result = this.sponsorshipRepository.save(sponsorship);

		if (sponsorship.getId() == 0) {
			sponsor = this.sponsorService.findByPrincipal();
			sponsor.getSponsorships().add(result);
			this.sponsorService.save(sponsor);

		}

		return result;
	}
	public Collection<Sponsorship> findByParadeId(final int paradeId) {
		Collection<Sponsorship> result;
		result = this.sponsorshipRepository.findByParadeId(paradeId);
		return result;
	}
	public Collection<Sponsorship> getExpiredSponsorships() {
		final Collection<Sponsorship> result;
		final Date date = new Date();
		final Integer year = date.getYear() + 1900;
		final String yearString = year.toString().substring(2, 4);
		final Integer expirationYear = new Integer(yearString);
		final int expirationMonth = date.getMonth() + 1;
		result = this.sponsorshipRepository.findExpiredCreditCards(expirationYear, expirationMonth);
		return result;
	}

	public void disableExpiredSponsorships() {
		final Collection<Sponsorship> expiredSponsorships = this.getExpiredSponsorships();
		for (final Sponsorship sponsorship : expiredSponsorships) {
			sponsorship.setActive(false);
			this.sponsorshipRepository.save(sponsorship);
		}
	}

	public Sponsorship disable(final int sponsorshipId) {
		Sponsorship result;

		final Sponsorship sponsorship = this.findOne(sponsorshipId);
		Assert.isTrue(this.sponsorService.findByPrincipal().getSponsorships().contains(sponsorship));
		sponsorship.setActive(false);
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}

	public Sponsorship enable(final int sponsorshipId) {
		Sponsorship result;

		final Sponsorship sponsorship = this.findOne(sponsorshipId);
		Assert.isTrue(this.sponsorService.findByPrincipal().getSponsorships().contains(sponsorship));
		sponsorship.setActive(true);
		result = this.sponsorshipRepository.save(sponsorship);

		return result;

	}

	public Sponsorship randomSponsorshipFromParade(final int id) {
		final Sponsorship result;
		final List<Sponsorship> sponsorships = new ArrayList<Sponsorship>(this.sponsorshipRepository.findActiveByParadeId(id));
		if (sponsorships.isEmpty())
			return null;
		final Random random = new Random();
		final int randomInt = random.nextInt(sponsorships.size());
		final Sponsorship sponsorship = sponsorships.get(randomInt);
		final double fare = this.systemConfigService.findSystemConfiguration().getFareCharge();

		sponsorship.setFare(sponsorship.getFare() + fare);
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}

	public void flush() {
		this.sponsorshipRepository.flush();

	}
}
