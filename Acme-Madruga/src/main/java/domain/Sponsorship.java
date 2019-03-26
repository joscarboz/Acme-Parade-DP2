
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private String	banner;
	private String	targetUrl;
	private Double	fare;
	private boolean	active;


	@SafeHtml
	@NotEmpty
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@SafeHtml
	@NotEmpty
	@URL
	public String getTargetUrl() {
		return this.targetUrl;
	}

	public void setTargetUrl(final String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Double getFare() {
		return this.fare;
	}

	public void setFare(final Double fare) {
		this.fare = fare;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}


	// Relations

	private Parade		parade;
	private CreditCard	creditCard;


	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(final Parade parade) {
		this.parade = parade;
	}

	@OneToOne(optional = false)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
