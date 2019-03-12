
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Access(AccessType.PROPERTY)
public class Chapter extends Actor {

	// Relations

	private Collection<Proclaim>	proclaims;
	private Area					area;


	@OneToMany
	public Collection<Proclaim> getProclaims() {
		return this.proclaims;
	}

	public void setProclaims(final Collection<Proclaim> proclaims) {
		this.proclaims = proclaims;
	}

	@OneToOne
	public Area getArea() {
		return this.area;
	}

	public void setArea(final Area area) {
		this.area = area;
	}

}
