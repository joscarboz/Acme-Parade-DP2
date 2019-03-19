
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	@Autowired
	private BrotherhoodService			brotherhoodService;
	@Autowired
	private HistoryService				historyService;
	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;


	//Constructor
	public InceptionRecordService() {
		super();
	}

	//CRUD methods

	public InceptionRecord create() {
		InceptionRecord result;
		result = new InceptionRecord();
		return result;
	}

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);
		InceptionRecord result;
		if (inceptionRecord.getId() == 0) {
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			final History history = this.historyService.create();
			history.setInceptionRecord(inceptionRecord);
			this.historyService.save(history);
			brotherhood.setHistory(history);
			this.historyService.flush();
			this.brotherhoodService.save(brotherhood);
			result = this.inceptionRecordRepository.save(inceptionRecord);

			this.flush();
			this.historyService.flush();
		} else {
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			final History history;
			history = brotherhood.getHistory();
			brotherhood.setHistory(history);
			result = this.inceptionRecordRepository.save(inceptionRecord);
			this.historyService.save(history);
			this.brotherhoodService.save(brotherhood);
			history.setInceptionRecord(result);
			this.flush();
			this.historyService.flush();
		}
		return result;
	}
	public void flush() {
		this.inceptionRecordRepository.flush();
	}
}
