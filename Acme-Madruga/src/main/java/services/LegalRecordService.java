
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.Brotherhood;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private LegalRecordRepository	legalRecordRepository;
	@Autowired
	private HistoryService			historyService;


	//Constructor
	public LegalRecordService() {
		super();
	}

	//CRUD methods

	public LegalRecord create() {
		LegalRecord result;
		result = new LegalRecord();
		return result;
	}

	public LegalRecord findOne(final int id) {
		final LegalRecord res = this.legalRecordRepository.findOne(id);
		return res;
	}

	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);

		LegalRecord result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		if (legalRecord.getId() != 0)
			Assert.isTrue(brotherhood.getHistory().getLegalRecords().contains(legalRecord));
		result = this.legalRecordRepository.save(legalRecord);
		if (legalRecord.getId() == 0) {
			this.legalRecordRepository.flush();
			brotherhood.getHistory().getLegalRecords().add(legalRecord);
			this.historyService.save(brotherhood.getHistory());
		}

		return result;
	}

	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		Assert.isTrue(this.legalRecordRepository.exists(legalRecord.getId()));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		brotherhood.getHistory().getLegalRecords().remove(legalRecord);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.legalRecordRepository.delete(legalRecord);
	}
	public void flush() {
		this.legalRecordRepository.flush();
	}
}
