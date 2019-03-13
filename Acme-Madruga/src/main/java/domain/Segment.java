package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {
	
	private String origin;
	private String destination;
	private Date originReachMoment;
	private Date destinationReachMoment;
	
	@NotBlank
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	@NotBlank
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getOriginReachMoment() {
		return originReachMoment;
	}
	public void setOriginReachMoment(Date originReachMoment) {
		this.originReachMoment = originReachMoment;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDestinationReachMoment() {
		return destinationReachMoment;
	}
	public void setDestinationReachMoment(Date destinationReachMoment) {
		this.destinationReachMoment = destinationReachMoment;
	}
	
	

}
