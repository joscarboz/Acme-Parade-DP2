package services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProclaimRepository;
import domain.Chapter;
import domain.Proclaim;
import java.util.Calendar;
import java.util.Collection;


@Service
@Transactional
public class ProclaimService {

	// Managed Repository
	@Autowired
	private ProclaimRepository	proclaimRepository;

	@Autowired
	private ChapterService		chapterService;


	public Proclaim create() {
		final Proclaim result = new Proclaim();

		result.setMoment(Calendar.getInstance().getTime());

		return result;

	}

	public Proclaim findOne(final int proclaimId) {
		Assert.isTrue(proclaimId != 0);

		Proclaim result;

		result = this.proclaimRepository.findOne(proclaimId);

		return result;
	}

	public Collection<Proclaim> findAll() {
		Collection<Proclaim> result;

		result = this.proclaimRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Proclaim save(final Proclaim proclaim) {

		Assert.notNull(proclaim);

		final Chapter chapter = this.chapterService.findByPrincipal();

		final Proclaim result = this.proclaimRepository.save(proclaim);

		if (proclaim.getId() == 0)
			chapter.getProclaims().add(result);

		this.chapterService.save(chapter);

		return result;
	}

	public void flush() {
		this.proclaimRepository.flush();
	}

}
