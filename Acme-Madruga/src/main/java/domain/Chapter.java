package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.PROPERTY)
public class Chapter extends Actor {

	// Relations

	private Collection<Proclaim> proclaims;
	private Collection<Area> areas;

	@OneToMany
	public Collection<Proclaim> getProclaims() {
		return proclaims;
	}

	public void setProclaims(Collection<Proclaim> proclaims) {
		this.proclaims = proclaims;
	}

	@OneToMany
	public Collection<Area> getAreas() {
		return areas;
	}

	public void setAreas(Collection<Area> areas) {
		this.areas = areas;
	}

}
