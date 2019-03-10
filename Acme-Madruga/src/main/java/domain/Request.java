
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "status")
})
public class Request extends DomainEntity {

	private String	status;
	private int		column;
	private int		row;
	private String	rejectionReason;


	@NotBlank
	public String getStatus() {
		return this.status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}

	@NotNull
	@Range(min = 0, max = 2)
	@Column(name = "`column`")
	public int getColumn() {
		return this.column;
	}
	public void setColumn(final int column) {
		this.column = column;
	}

	@NotNull
	@Min(0)
	@Column(name = "`row`")
	public int getRow() {
		return this.row;
	}
	public void setRow(final int row) {
		this.row = row;
	}

	public String getRejectionReason() {
		return this.rejectionReason;
	}
	public void setRejectionReason(final String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}


	//Relationships
	private Member		member;
	private Procession	procession;


	@ManyToOne(optional = false)
	@NotNull
	public Member getMember() {
		return this.member;
	}
	public void setMember(final Member member) {
		this.member = member;
	}

	@ManyToOne(optional = false)
	@NotNull
	public Procession getProcession() {
		return this.procession;
	}
	public void setProcession(final Procession procession) {
		this.procession = procession;
	}
}
