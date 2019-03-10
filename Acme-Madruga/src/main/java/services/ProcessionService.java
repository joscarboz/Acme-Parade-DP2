
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

import repositories.ProcessionRepository;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Member;
import domain.Procession;
import domain.Request;

@Service
@Transactional
public class ProcessionService {

	@Autowired
	private ProcessionRepository	processionRepository;

	@Autowired
	private SystemConfigService		systemConfigService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private Validator				validator;


	public ProcessionService() {
		super();
	}

	public Collection<Procession> findAll() {
		Collection<Procession> result;

		result = this.processionRepository.findAll();

		return result;
	}

	public Procession findOne(final int processionId) {
		Assert.isTrue(processionId != 0);

		Procession result;

		result = this.processionRepository.findOne(processionId);

		return result;
	}

	public Procession create() {

		Assert.isTrue(!this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea"));
		final Procession result = new Procession();
		result.setTicker(this.systemConfigService.generateTicker());

		result.setFloats(new LinkedList<Float>());

		result.setRequests(new LinkedList<Request>());

		return result;
	}

	public Procession save(final Procession procession) {

		Assert.notNull(procession);
		Assert.isTrue(procession.getMoment().after(Calendar.getInstance().getTime()));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final Procession result = this.processionRepository.save(procession);
		if (procession.getId() == 0) {
			brotherhood.getProcessions().add(result);
			this.brotherhoodService.save(brotherhood);

		}
		return result;
	}

	public void delete(final Procession procession) {

		Assert.notNull(procession);

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final Collection<Procession> processions = brotherhood.getProcessions();

		Assert.isTrue(processions.contains(procession));

		final Collection<Request> requests = new HashSet<Request>(procession.getRequests());
		Assert.isTrue(procession.isDraftMode());
		for (final Request r : requests)
			this.requestService.delete(r);

		this.requestService.flush();

		processions.remove(procession);
		this.brotherhoodService.save(brotherhood);
		this.brotherhoodService.flush();
		this.processionRepository.delete(procession);
	}

	// Finder Methods

	public Collection<Procession> findByKeyword(final Finder finder) {
		List<Procession> result = new ArrayList<Procession>();
		if (finder.getKeyWord().isEmpty()) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else
			result.addAll(this.processionRepository.findByKeyword(finder.getKeyWord()));

		return result;
	}

	public Collection<Procession> findByArea(final Finder finder) {
		List<Procession> result = new ArrayList<Procession>();
		if (finder.getAreaName().isEmpty()) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else
			result.addAll(this.processionRepository.findByArea(finder.getAreaName()));

		return result;
	}

	public Collection<Procession> findByDate(final Finder finder) {
		List<Procession> result = new ArrayList<Procession>();
		if (finder.getMinDate() == null && finder.getMaxDate() == null) { //TODO: Revisar
			result.addAll(this.findAll());
			if (result.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
				result = result.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		} else {
			if (finder.getMinDate() == null) {
				final Date minimumDate = new Date(0);
				minimumDate.setYear(0);
				final Date maximumDate = finder.getMaxDate();
				result.addAll(this.processionRepository.findByDate(minimumDate, maximumDate));
			}

			if (finder.getMaxDate() == null) {
				final Date maximumDate = new Date(0);
				maximumDate.setYear(500);
				final Date minimumDate = finder.getMinDate();
				result.addAll(this.processionRepository.findByDate(minimumDate, maximumDate));
			} else {
				final Date minimumDate = finder.getMinDate();
				final Date maximumDate = finder.getMaxDate();
				result.addAll(this.processionRepository.findByDate(minimumDate, maximumDate));
			}

		}
		return result;
	}

	public Collection<Procession> findAvailable(final Member member) {
		Assert.isTrue(member.getId() != 0);
		Assert.notNull(member);

		final Collection<Procession> res = new ArrayList<>();
		final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findByActiveMember(member.getId());
		final Collection<Procession> memberProcessions = this.processionRepository.findByMemberId(member.getId());

		for (final Brotherhood b : brotherhoods)
			res.addAll(b.getProcessions());

		res.removeAll(memberProcessions);

		Assert.notNull(res);

		return res;

	}
	public Procession reconstruct(final Procession procession, final BindingResult bindingResult) {
		Procession result;

		if (procession.getId() == 0)
			result = procession;
		else {
			final Procession p = this.processionRepository.findOne(procession.getId());
			result = new Procession(p);
			result.setId(p.getId());
			result.setVersion(p.getVersion());
			result.setTitle(procession.getTitle());
			result.setDescription(procession.getDescription());
			result.setDraftMode(procession.isDraftMode());
			result.setMoment(procession.getMoment());
			result.setFloats(procession.getFloats());
			this.validator.validate(result, bindingResult);

		}
		return result;

	}

	// Update so the member can delete a Request

	public Procession update(final Procession procession) {

		Assert.notNull(procession);

		return this.processionRepository.save(procession);
	}

}
