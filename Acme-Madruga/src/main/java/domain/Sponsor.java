package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsor extends Actor {

	// Relations

	private Collection<Sponsorship> sponshorships;

	@OneToMany
	public Collection<Sponsorship> getSponshorships() {
		return sponshorships;
	}

	public void setSponshorships(Collection<Sponsorship> sponshorships) {
		this.sponshorships = sponshorships;
	}

}
