
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Brotherhood;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	@Autowired
	private BrotherhoodService				brotherhoodService;
	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;
	@Autowired
	private HistoryService					historyService;


	//Constructor
	public MiscellaneousRecordService() {
		super();
	}

	//CRUD methods

	public MiscellaneousRecord create() {
		MiscellaneousRecord result;
		result = new MiscellaneousRecord();
		return result;
	}

	public MiscellaneousRecord findOne(final int id) {
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.findOne(id);
		return res;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);

		MiscellaneousRecord result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		if (miscellaneousRecord.getId() != 0)
			Assert.isTrue(brotherhood.getHistory().getMiscellaneousRecords().contains(miscellaneousRecord));
		result = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		if (miscellaneousRecord.getId() == 0) {
			this.miscellaneousRecordRepository.flush();
			brotherhood.getHistory().getMiscellaneousRecords().add(miscellaneousRecord);
			this.historyService.save(brotherhood.getHistory());
		}

		return result;
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		Assert.isTrue(miscellaneousRecord.getId() != 0);
		Assert.isTrue(this.miscellaneousRecordRepository.exists(miscellaneousRecord.getId()));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		brotherhood.getHistory().getMiscellaneousRecords().remove(miscellaneousRecord);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}
	public void flush() {
		this.miscellaneousRecordRepository.flush();
	}
}
