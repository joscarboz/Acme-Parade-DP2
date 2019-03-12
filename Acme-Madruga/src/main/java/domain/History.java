package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	// Relations

	private InceptionRecord inceptionRecord;
	private Collection<PeriodRecord> periodRecords;
	private Collection<LegalRecord> legalRecords;
	private Collection<LinkRecord> linkRecords;
	private Collection<MiscellaneousRecord> miscellaneousRecords;

	@OneToOne(optional = false)
	public InceptionRecord getInceptionRecord() {
		return inceptionRecord;
	}

	public void setInceptionRecord(InceptionRecord inceptionRecord) {
		this.inceptionRecord = inceptionRecord;
	}

	@OneToMany
	public Collection<PeriodRecord> getPeriodRecords() {
		return periodRecords;
	}

	public void setPeriodRecords(Collection<PeriodRecord> periodRecords) {
		this.periodRecords = periodRecords;
	}

	@OneToMany
	public Collection<LegalRecord> getLegalRecords() {
		return legalRecords;
	}

	public void setLegalRecords(Collection<LegalRecord> legalRecords) {
		this.legalRecords = legalRecords;
	}

	@OneToMany
	public Collection<LinkRecord> getLinkRecords() {
		return linkRecords;
	}

	public void setLinkRecords(Collection<LinkRecord> linkRecords) {
		this.linkRecords = linkRecords;
	}

	@OneToMany
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return miscellaneousRecords;
	}

	public void setMiscellaneousRecords(
			Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

}
