
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "position,dropOutDate")
})
public class Enrolment extends DomainEntity {

	private Date		moment;
	private Position	position;
	private Date		dropOutDate;


	public Enrolment() {

	}

	public Enrolment(final Enrolment e) {
		super();
		this.moment = e.getMoment();
		this.position = e.getPosition();
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@ManyToOne(optional = true)
	@Valid
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDropOutDate() {
		return this.dropOutDate;
	}

	public void setDropOutDate(final Date dropOutDate) {
		this.dropOutDate = dropOutDate;
	}


	// Relationships

	private Member		member;
	private Brotherhood	brotherhood;


	@ManyToOne(optional = false)
	@NotNull
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@NotNull
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

}
