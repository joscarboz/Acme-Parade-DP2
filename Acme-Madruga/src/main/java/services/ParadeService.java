
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Member;
import domain.Parade;
import domain.Request;
import domain.Segment;

@Service
@Transactional
public class ParadeService {

	@Autowired
	private ParadeRepository	paradeRepository;

	@Autowired
	private SystemConfigService	systemConfigService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private Validator			validator;


	public ParadeService() {
		super();
	}

	public Collection<Parade> findAll() {
		Collection<Parade> result;

		result = this.paradeRepository.findAll();

		return result;
	}

	public Parade findOne(final int paradeId) {
		Assert.isTrue(paradeId != 0);

		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		return result;
	}

	public Parade create() {

		Assert.isTrue(!this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea"));
		final Parade result = new Parade();
		result.setTicker(this.systemConfigService.generateTicker());

		result.setFloats(new LinkedList<Float>());

		result.setRequests(new LinkedList<Request>());

		result.setSegments(new LinkedList<Segment>());
		result.setStatus("");
		result.setRejectionReason("");
		return result;
	}

	public Parade save(final Parade parade) {

		Assert.notNull(parade);
		Assert.isTrue(parade.getMoment().after(Calendar.getInstance().getTime()));
		if (parade.isDraftMode())
			parade.setStatus("");
		if ((!parade.isDraftMode()) && (!parade.getStatus().equals("accepted") && !parade.getStatus().equals("rejected")))
			parade.setStatus("submitted");
		final Parade result = this.paradeRepository.save(parade);
		if (parade.getId() == 0) {
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			brotherhood.getParades().add(result);
			this.brotherhoodService.save(brotherhood);

		}
		return result;
	}

	public void delete(final Parade parade) {

		Assert.notNull(parade);

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final Collection<Parade> parades = brotherhood.getParades();

		Assert.isTrue(parades.contains(parade));

		final Collection<Request> requests = new HashSet<Request>(parade.getRequests());
		Assert.isTrue(parade.isDraftMode());
		for (final Request r : requests)
			this.requestService.delete(r);

		this.requestService.flush();

		parades.remove(parade);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.paradeRepository.delete(parade);
	}
	public void flush() {
		this.paradeRepository.flush();
	}

	// Finder Methods

	public Collection<Parade> findByKeyword(final Finder finder) {
		List<Parade> result = new ArrayList<Parade>();
		if (finder.getKeyWord().isEmpty()) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else
			result.addAll(this.paradeRepository.findByKeyword(finder.getKeyWord()));

		return result;
	}

	public Collection<Parade> findByArea(final Finder finder) {
		List<Parade> result = new ArrayList<Parade>();
		if (finder.getAreaName().isEmpty()) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else
			result.addAll(this.paradeRepository.findByArea(finder.getAreaName()));

		return result;
	}

	public Collection<Parade> findByDate(final Finder finder) {
		List<Parade> result = new ArrayList<Parade>();
		if (finder.getMinDate() == null && finder.getMaxDate() == null) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else {
			if (finder.getMinDate() == null) {
				final Date minimumDate = new Date(0);
				minimumDate.setYear(0);
				final Date maximumDate = finder.getMaxDate();
				result.addAll(this.paradeRepository.findByDate(minimumDate, maximumDate));
			}

			if (finder.getMaxDate() == null) {
				final Date maximumDate = new Date(0);
				maximumDate.setYear(500);
				final Date minimumDate = finder.getMinDate();
				result.addAll(this.paradeRepository.findByDate(minimumDate, maximumDate));
			} else {
				final Date minimumDate = finder.getMinDate();
				final Date maximumDate = finder.getMaxDate();
				result.addAll(this.paradeRepository.findByDate(minimumDate, maximumDate));
			}

		}
		return result;
	}

	public Collection<Parade> findAvailable(final Member member) {
		Assert.isTrue(member.getId() != 0);
		Assert.notNull(member);

		final Collection<Parade> res = new ArrayList<>();
		final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findByActiveMember(member.getId());
		final Collection<Parade> memberParades = this.paradeRepository.findByMemberId(member.getId());

		for (final Brotherhood b : brotherhoods)
			res.addAll(b.getParades());

		res.removeAll(memberParades);

		Assert.notNull(res);

		return res;

	}
	public Parade reconstruct(final Parade parade, final BindingResult bindingResult) {
		Parade result;

		if (parade.getId() == 0)
			result = parade;
		else {
			final Parade p = this.paradeRepository.findOne(parade.getId());
			result = new Parade(p);
			result.setId(p.getId());
			result.setVersion(p.getVersion());
			result.setTitle(parade.getTitle());
			result.setDescription(parade.getDescription());
			result.setDraftMode(parade.isDraftMode());
			result.setMoment(parade.getMoment());
			result.setFloats(parade.getFloats());
			result.setStatus(parade.getStatus());
			this.validator.validate(result, bindingResult);

		}
		return result;

	}

	// Update so the member can delete a Request

	public Parade update(final Parade parade) {

		Assert.notNull(parade);

		return this.paradeRepository.save(parade);
	}

}
