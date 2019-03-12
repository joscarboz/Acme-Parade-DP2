package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "moment, draftMode") })
public class Parade extends DomainEntity {

	private String ticker;
	private String title;
	private String description;
	private Date moment;
	private boolean draftMode;
	private String status;

	public Parade() {

	}

	public Parade(final Parade p) {
		super();
		this.ticker = p.getTicker();
		this.title = p.getTitle();
		this.description = p.getDescription();
		this.moment = p.getMoment();
		this.draftMode = p.isDraftMode();
		this.floats = p.getFloats();
		this.requests = p.getRequests();
	}

	@NotBlank
	@Pattern(regexp = "[0-9]{6}-[A-Z]{5}")
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotNull
	public boolean isDraftMode() {
		return this.draftMode;
	}

	public void setDraftMode(final boolean draftMode) {
		this.draftMode = draftMode;
	}

	// Relationships
	private Collection<Float> floats;
	private Collection<Request> requests;
	private Collection<Segment> segments;

	@OneToMany
	@NotEmpty
	public Collection<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final Collection<Float> floats) {
		this.floats = floats;
	}

	@OneToMany(mappedBy = "parade", cascade = CascadeType.PERSIST)
	public Collection<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(final Collection<Request> requests) {
		this.requests = requests;
	}

	@NotBlank
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@OneToMany
	public Collection<Segment> getSegments() {
		return segments;
	}

	public void setSegments(Collection<Segment> segments) {
		this.segments = segments;
	}

}
