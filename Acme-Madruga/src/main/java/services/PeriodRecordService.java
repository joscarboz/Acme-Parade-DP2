
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.Brotherhood;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;
	@Autowired
	private HistoryService			historyService;


	//Constructor
	public PeriodRecordService() {
		super();
	}

	//CRUD methods

	public PeriodRecord create() {
		PeriodRecord result;
		result = new PeriodRecord();
		return result;
	}

	public PeriodRecord findOne(final int id) {
		final PeriodRecord res = this.periodRecordRepository.findOne(id);
		return res;
	}

	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getStartYear() <= periodRecord.getEndYear());

		PeriodRecord result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		if (periodRecord.getId() != 0)
			Assert.isTrue(brotherhood.getHistory().getPeriodRecords().contains(periodRecord));
		result = this.periodRecordRepository.save(periodRecord);
		if (periodRecord.getId() == 0) {
			this.periodRecordRepository.flush();
			brotherhood.getHistory().getPeriodRecords().add(periodRecord);
			this.historyService.save(brotherhood.getHistory());
		}

		return result;
	}

	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);
		Assert.isTrue(this.periodRecordRepository.exists(periodRecord.getId()));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		brotherhood.getHistory().getPeriodRecords().remove(periodRecord);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.periodRecordRepository.delete(periodRecord);
	}
	public void flush() {
		this.periodRecordRepository.flush();
	}
}
