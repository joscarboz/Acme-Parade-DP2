
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	private String	origin;
	private String	destination;
	private Date	originReachMoment;
	private Date	destinationReachMoment;


	@NotBlank
	@Pattern(regexp = "^[-]?[0-9]+[.]?[0-9]+[,][-]?[0-9]+[.]?[0-9]+$", message = "Coordinates not valid")
	public String getOrigin() {
		return this.origin;
	}
	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	@NotBlank
	@Pattern(regexp = "^[-]?[0-9]+[.]?[0-9]+[,][-]?[0-9]+[.]?[0-9]+$", message = "Coordinates not valid")
	public String getDestination() {
		return this.destination;
	}
	public void setDestination(final String destination) {
		this.destination = destination;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getOriginReachMoment() {
		return this.originReachMoment;
	}
	public void setOriginReachMoment(final Date originReachMoment) {
		this.originReachMoment = originReachMoment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDestinationReachMoment() {
		return this.destinationReachMoment;
	}
	public void setDestinationReachMoment(final Date destinationReachMoment) {
		this.destinationReachMoment = destinationReachMoment;
	}

}
