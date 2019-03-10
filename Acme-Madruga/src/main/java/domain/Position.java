package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	private String title;
	private String spanishTitle;

	@SafeHtml
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@SafeHtml
	@NotBlank
	public String getSpanishTitle() {
		return spanishTitle;
	}

	public void setSpanishTitle(String spanishTitle) {
		this.spanishTitle = spanishTitle;
	}

}
