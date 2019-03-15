
package services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryRepository		historyRepository;

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	public HistoryService() {
		super();
	}

	public History create() {
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getHistory() == null);
		History result;
		result = new History();
		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		final Collection<PeriodRecord> periodRecords = Collections.EMPTY_SET;
		final Collection<LinkRecord> linkRecords = Collections.EMPTY_SET;
		final Collection<MiscellaneousRecord> miscellaneousRecords = Collections.EMPTY_SET;
		final Collection<LegalRecord> legalRecords = Collections.EMPTY_SET;
		result.setInceptionRecord(inceptionRecord);
		result.setLegalRecords(legalRecords);
		result.setLinkRecords(linkRecords);
		result.setMiscellaneousRecords(miscellaneousRecords);
		result.setPeriodRecords(periodRecords);
		return result;

	}

	public History save(final History history) {
		Assert.notNull(history);
		final History result = this.historyRepository.save(history);
		return result;
	}

	public void flush() {
		this.historyRepository.flush();
	}

}
