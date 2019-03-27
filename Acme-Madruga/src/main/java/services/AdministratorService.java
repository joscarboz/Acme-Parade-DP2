package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import repositories.AreaRepository;
import repositories.BoxRepository;
import repositories.BrotherhoodRepository;
import repositories.ChapterRepository;
import repositories.FinderRepository;
import repositories.HistoryRepository;
import repositories.MessageRepository;
import repositories.ParadeRepository;
import repositories.RequestRepository;
import repositories.SponsorRepository;
import repositories.SponsorshipRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Administrator;
import domain.Area;
import domain.Box;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.Position;
import domain.SocialProfile;
import domain.Sponsor;
import forms.RegisterAdminForm;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository
	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private BrotherhoodRepository brotherhoodRepository;
	@Autowired
	private BoxRepository boxRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private FinderRepository finderRepository;
	// Supporting Services
	@Autowired
	private SystemConfigService systemConfigService;

	// Es probable que todo lo referente a history debiera ir en el servicio de
	// history, al no estar creado de momento se hará referencia al repositorio
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private BoxService boxService;
	@Autowired
	private MemberService memberService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ParadeRepository paradeRepository;

	@Autowired
	private SponsorRepository sponsorRepository;

	@Autowired
	private SponsorshipRepository sponsorshipRepository;

	// CRUD
	public Administrator findOne(final int administratorId) {
		return this.administratorRepository.findOne(administratorId);
	}

	public UserAccount findByActor(int actorId) {
		UserAccount result;
		result = this.userAccountRepository.findById(actorId);

		return result;

	}

	public Collection<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	public Administrator save(final Administrator administrator) {
		return this.administratorRepository.save(administrator);
	}

	public Administrator create() {
		Administrator result;

		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		result = new Administrator();

		final Collection<Box> defaultBoxes = this.boxService
				.createDefaultBoxes();
		this.boxRepository.save(defaultBoxes);

		this.boxService.flush();
		final List<Box> allBoxes = new ArrayList<Box>(this.boxService.findAll());
		final List<Box> boxes = allBoxes.subList(allBoxes.size() - 5,
				allBoxes.size());
		result.setBoxes(boxes);
		result.setSpammer(false);
		result.setSocialProfiles(new HashSet<SocialProfile>());
		return result;

	}

	public Administrator register(final RegisterAdminForm registerMemberForm) {
		final Administrator result = this.create();

		final UserAccount userAccount = new UserAccount();
		final String password = registerMemberForm.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		userAccount.setPassword(hashedPassword);
		userAccount.setUsername(registerMemberForm.getUsername());
		userAccount.setAccountNonLocked(true);

		// Seteo los atributos al resultado
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);
		result.setAddress(registerMemberForm.getAddress());
		result.setMiddleName(registerMemberForm.getMiddleName());
		result.setEmail(registerMemberForm.getEmail());
		result.setName(registerMemberForm.getName());
		result.setPhone(registerMemberForm.getPhone());
		result.setPhoto(registerMemberForm.getPhoto());
		result.setSurname(registerMemberForm.getSurname());

		this.save(result);

		return result;
	}

	// Stats

	public int countPositiveWords(final String string) {
		int result = 0;
		final Collection<String> positiveWords = this.systemConfigService
				.findSystemConfiguration().getPositiveWords();
		final String[] words = string.replaceAll("[^A-Za-z0-9 ]", "")
				.split(" ");
		for (final String s : words)
			if (positiveWords.contains(s))
				result++;
		return result;
	}

	public int countNegativeWords(final String string) {
		int result = 0;
		final Collection<String> negativeWords = this.systemConfigService
				.findSystemConfiguration().getNegativeWords();
		final String[] words = string.replaceAll("[^A-Za-z0-9 ]", "")
				.split(" ");
		for (final String s : words)
			if (negativeWords.contains(s))
				result++;
		return result;
	}

	public Double getScore(final Actor actor) {
		double positiveWords = 0.;
		double negativeWords = 0.;
		double score;
		String string;
		String lower;
		final Collection<Message> m = this.messageRepository.findbySender(actor
				.getId());

		for (final Message message : m) {
			string = message.getSubject() + " " + message.getBody();

			if (string != null) {
				lower = string.toLowerCase();
				positiveWords += this.countPositiveWords(lower);
				negativeWords += this.countNegativeWords(lower);
			}
		}

		if (positiveWords == 0 && negativeWords == 0)
			score = 0;
		else if (positiveWords == 0)
			score = -1;
		else if (negativeWords == 0)
			score = 1;
		else {
			final double totalWords = positiveWords + negativeWords;
			score = ((positiveWords / totalWords) - 0.5) * 2;
		}
		return score;
	}

	// Dashboard

	public Double[] membersPerBrotherhoodStats() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.administratorRepository.getMembersPerBrotherhood();
	}

	public Collection<Brotherhood> largestBrotherhoods() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		final List<Brotherhood> result = new ArrayList<Brotherhood>(
				this.brotherhoodRepository.findLargestBrotherhoods());
		if (result.size() > 3)
			return result.subList(0, 3);
		return result;
	}

	public Collection<Brotherhood> smallestBrotherhoods() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		final List<Brotherhood> result = new ArrayList<Brotherhood>(
				this.brotherhoodRepository.findSmallestBrotherhoods());
		if (result.size() > 3)
			return result.subList(0, 3);
		return result;
	}

	public Double acceptedRequestsRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.administratorRepository.approvedRequestRatio();
	}

	public Double rejectedRequestsRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.administratorRepository.rejectedRequestRatio();
	}

	public Double pendingRequestsRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.administratorRepository.pendingRequestRatio();
	}

	public Collection<Parade> upcomingParades() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.administratorRepository.upcomingParades();
	}

	public Map<Position, Long> positionHistogram() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		HashMap<Position, Long> pos = new HashMap<Position, Long>();
		LinkedList<Position> posList = new LinkedList<Position>(
				administratorRepository.positionHistogram());

		LinkedList<Long> numList = new LinkedList<Long>(
				administratorRepository.longHistogram());

		for (int i = 0; i < posList.size(); i++) {

			pos.put(posList.get(i), numList.get(i));
		}

		return pos;
	}

	public Map<Area, Double[]> brotherhoodsPerArea() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		HashMap<Area, Double[]> statistics = new HashMap<Area, Double[]>();
		LinkedList<Area> areaList = new LinkedList<Area>(
				this.administratorRepository.BrotherhoodsPerArea());

		LinkedList<Long> numList = new LinkedList<Long>(
				administratorRepository.BrotherhoodsPerAreaNumber());

		Double sum = 0.;

		for (Long l : numList) {

			sum = sum + l;
		}

		for (int i = 0; i < areaList.size(); i++) {

			Double[] ratioCount = new Double[2];

			ratioCount[0] = numList.get(i) / sum;

			ratioCount[1] = numList.get(i).doubleValue();

			statistics.put(areaList.get(i), ratioCount);
		}

		return statistics;
	}

	public Double[] brotherhoodsPerAreaStatistics() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		Double[] statistics = new Double[4];

		LinkedList<Long> numList = new LinkedList<Long>(
				administratorRepository.BrotherhoodsPerAreaNumber());

		if (numList.isEmpty()) {

			statistics[0] = 0.;
			statistics[1] = 0.;
			statistics[2] = 0.;
			statistics[3] = 0.;

			return statistics;

		} else {
			statistics[0] = Collections.min(numList).doubleValue();

			statistics[1] = Collections.max(numList).doubleValue();

			Double sum = 0.;

			for (Long l : numList) {

				sum = sum + l;
			}

			statistics[2] = sum / numList.size();

			// Desviación estándar

			Double sumaValores = 0.;

			for (int i = 0; i < numList.size(); i++) {

				sumaValores = sumaValores
						+ Math.pow(Math.abs(numList.get(i) - statistics[2]), 2);
			}

			statistics[3] = Math.sqrt(sumaValores / numList.size());

			return statistics;
		}
	}

	public Collection<Member> acceptedRequestMembers() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		final List<Member> result = new ArrayList<Member>();

		for (final Member m : this.memberService.findAll()) {
			final double requests = this.requestRepository.countByMember(m
					.getId());
			final double acceptedRequests = this.requestRepository
					.countAcceptedByMember(m.getId());
			if (acceptedRequests > (requests / 10))
				result.add(m);
		}

		return result;
	}

	public Double[] getNumberResultFinders() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.finderRepository.getNumberResultFinders();
	}

	public Double countEmptyFinders() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.finderRepository.countEmptyFinders();
	}

	public Double countNonEmptyFinders() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return 1. - this.finderRepository.countEmptyFinders();
	}

	public Double[] recordsPerHistory() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.historyRepository.getRecordsPerHistory();
	}

	public Collection<Brotherhood> largestHistory() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		LinkedList<Brotherhood> brotherhoods = new LinkedList<Brotherhood>();
		for (Object[] b : this.historyRepository.largestHistory()) {

			brotherhoods.add((Brotherhood) b[1]);
		}
		return brotherhoods;
	}

	public Collection<Brotherhood> getLargestBrotherhoodAverageHistory() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.historyRepository.largerAverage();
	}

	public void flush() {
		this.administratorRepository.flush();
	}

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Administrator findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Administrator result;

		result = this.administratorRepository.findbyUserAccountID(userAccount
				.getId());

		return result;
	}

	public Double getNonCoordinateAreas() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		Double coord = this.areaRepository.coordinateAreasRatio();

		Double nonCord = 1.0 - coord;

		return nonCord;

	}

	public Double[] paradesPerChapter() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		return this.areaRepository.getParadesPerChapter();
	}

	public Collection<Chapter> chaptersParadesMoreAvg() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.chapterRepository.chapterMoreAVG();

	}

	public Double getDraftModeParadesRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.paradeRepository.draftModeParadesRatio();

	}

	public Double getFinalModeAcceptedParadesRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.paradeRepository.finalModeAcceptedParadesRatio();

	}

	public Double getFinalModeRejectedParadesRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.paradeRepository.finalModeRejectedParadesRatio();

	}

	public Double getFinalModeSubmittedParadesRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.paradeRepository.finalModeSubmittedParadesRatio();

	}

	public Double getActiveSponsorshipRatio() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.sponsorshipRepository.activeSponsorshipRatio();

	}

	public Double[] getActiveSponsorshipPerSponsor() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		return this.sponsorRepository.activeSponsorshipPerSponsor();

	}

	public Collection<Sponsor> getTopSponsorSponsorshipActive() {
		Administrator logged = this.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);

		List<Sponsor> result = new ArrayList<Sponsor>(
				this.sponsorRepository.findTopSponsorSponsorshipActive());
		if (result.size() > 5) {
			return result.subList(0, 5);
		}
		return result;

	}
}
