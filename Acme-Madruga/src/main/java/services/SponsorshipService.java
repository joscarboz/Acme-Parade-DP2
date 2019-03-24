
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.SponsorshipRepository;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	SponsorshipRepository	sponsorshipRepository;


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
