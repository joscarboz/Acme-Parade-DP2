
package services;

import java.util.Collection;
import java.util.Date;

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

	public Sponsorship save(final Sponsorship sponsorship, CreditCard creditCard) {
		Sponsorship result;
		Sponsor sponsor;
		Assert.notNull(sponsorship);
		Assert.notNull(creditCard);

		this.creditCardService.save(creditCard);
		this.creditCardService.flush();
		creditCard = this.creditCardService.findByNumber(creditCard.getNumber());
		sponsorship.setCreditCard(creditCard);
		result = this.sponsorshipRepository.save(sponsorship);
		sponsor = (Sponsor) this.actorService.findByPrincipal();
		sponsor.getSponsorships().add(result);

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
}
