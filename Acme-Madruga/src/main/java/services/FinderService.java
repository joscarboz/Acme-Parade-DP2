
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed Repository
	@Autowired
	private FinderRepository	finderRepository;

	// Supporting Services
	@Autowired
	private MemberService		memberService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private SystemConfigService	systemConfigService;


	// Constructor
	public FinderService() {
		super();
	}

	// CRUD

	public Finder create() {
		Finder result;
		Member member;

		result = new Finder();
		result.setResult(new HashSet<Procession>());
		result.setKeyWord("");
		result.setAreaName("");
		result.setFinderTime(new Date());

		member = this.memberService.findByPrincipal();
		result.setMember(member);
		return result;
	}

	public Collection<Finder> findAll() {
		Collection<Finder> result;

		result = this.finderRepository.findAll();

		return result;

	}

	public Finder save(final Finder finder) {
		Finder result;

		Assert.notNull(finder);
		List<Procession> findByKeyword = new ArrayList<Procession>(this.processionService.findByKeyword(finder));
		final List<Procession> findByArea = new ArrayList<Procession>(this.processionService.findByArea(finder));
		final List<Procession> findByDate = new ArrayList<Procession>(this.processionService.findByDate(finder));
		findByKeyword.retainAll(findByArea);
		findByKeyword.retainAll(findByDate);
		if (findByKeyword.size() > this.systemConfigService.findSystemConfiguration().getFinderMaxResults())
			findByKeyword = findByKeyword.subList(0, this.systemConfigService.findSystemConfiguration().getFinderMaxResults());
		finder.setResult(findByKeyword);
		finder.setMember(this.memberService.findByPrincipal());
		result = this.finderRepository.save(finder);
		return result;
	}
	public void delete(final Finder finder) {
		Assert.notNull(finder);

		this.finderRepository.delete(finder);

	}

	public Finder findOne(final Integer finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);

		return result;
	}

	// Other Business Methods

	public Finder findByPrincipal() {
		Finder result;
		Member member;

		member = this.memberService.findByPrincipal();
		result = this.findByMember(member);

		return result;
	}

	public Finder findByMember(final Member member) {
		Assert.notNull(member);

		Finder result;

		result = this.finderRepository.findByMemberID(member.getId());

		return result;
	}

	public void flush() {
		this.finderRepository.flush();
	}

	public Double[] finderStatistics() {
		return this.finderRepository.getNumberResultFinders();
	}

	public Double ratioEmptyFinders() {

		return this.finderRepository.countEmptyFinders();
	}
}
