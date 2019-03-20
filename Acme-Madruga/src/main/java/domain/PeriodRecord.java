
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends DomainEntity {

	private String				description;
	private Collection<String>	pictures;
	private int					startYear;
	private int					endYear;
	private String				title;


	@SafeHtml
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@ElementCollection
	@NotEmpty
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Range(min = 0)
	public int getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final int startYear) {
		this.startYear = startYear;
	}

	@Range(min = 0)
	public int getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final int endYear) {
		this.endYear = endYear;
	}

	@SafeHtml
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

}
