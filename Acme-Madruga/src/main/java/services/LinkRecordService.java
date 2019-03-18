
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.Brotherhood;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private LinkRecordRepository	linkRecordRepository;
	@Autowired
	private HistoryService			historyService;


	//Constructor
	public LinkRecordService() {
		super();
	}

	//CRUD methods

	public LinkRecord create() {
		LinkRecord result;
		result = new LinkRecord();
		return result;
	}

	public LinkRecord findOne(final int id) {
		final LinkRecord res = this.linkRecordRepository.findOne(id);
		return res;
	}

	public LinkRecord save(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);

		LinkRecord result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		if (linkRecord.getId() != 0)
			Assert.isTrue(brotherhood.getHistory().getLinkRecords().contains(linkRecord));
		result = this.linkRecordRepository.save(linkRecord);
		if (linkRecord.getId() == 0) {
			this.linkRecordRepository.flush();
			brotherhood.getHistory().getLinkRecords().add(linkRecord);
			this.historyService.save(brotherhood.getHistory());
		}

		return result;
	}

	public void delete(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		Assert.isTrue(this.linkRecordRepository.exists(linkRecord.getId()));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		brotherhood.getHistory().getLinkRecords().remove(linkRecord);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.linkRecordRepository.delete(linkRecord);
	}
	public void flush() {
		this.linkRecordRepository.flush();
	}
}
