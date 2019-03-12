package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Chapter extends Actor {

	private String title;


	@SafeHtml
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// Relations

	private Collection<Proclaim> proclaims;
	private Area area;

	@OneToMany
	public Collection<Proclaim> getProclaims() {
		return proclaims;
	}

	public void setProclaims(Collection<Proclaim> proclaims) {
		this.proclaims = proclaims;
	}

	@OneToOne(optional = false)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

}
