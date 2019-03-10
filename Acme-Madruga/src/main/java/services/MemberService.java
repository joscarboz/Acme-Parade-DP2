
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Box;
import domain.Enrolment;
import domain.Member;
import domain.Request;
import domain.SocialProfile;
import forms.RegisterMemberForm;

@Service
@Transactional
public class MemberService {

	// Managed Repository ----------------------------------------------------

	@Autowired
	private MemberRepository	memberRepository;
	@Autowired
	private BoxRepository		boxRepository;
	// Supporting services ----------------------------------------------------
	@Autowired
	private BoxService			boxService;


	// Constructor ----------------------------------------------------

	public MemberService() {
		super();
	}

	public Member register(final RegisterMemberForm registerMemberForm) {
		final Member result = this.create();

		final UserAccount userAccount = new UserAccount();
		final String password = registerMemberForm.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		Assert.isTrue(registerMemberForm.getPassword().equals(registerMemberForm.getPassword2()));
		userAccount.setPassword(hashedPassword);
		userAccount.setUsername(registerMemberForm.getUsername());
		userAccount.setAccountNonLocked(true);

		//Seteo los atributos al resultado
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
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

	//CRUD

	public Member create() {
		Member result;

		result = new Member();

		final Collection<Box> defaultBoxes = this.boxService.createDefaultBoxes();
		this.boxRepository.save(defaultBoxes);

		this.boxService.flush();
		final List<Box> allBoxes = new ArrayList<Box>(this.boxService.findAll());
		final List<Box> boxes = allBoxes.subList(allBoxes.size() - 5, allBoxes.size());
		result.setBoxes(boxes);

		result.setSpammer(false);
		result.setEnrolments(new HashSet<Enrolment>());
		result.setRequests(new HashSet<Request>());
		result.setSocialProfiles(new HashSet<SocialProfile>());
		return result;

	}

	public Member save(final Member member) {
		Assert.notNull(member);
		Member result;
		result = this.memberRepository.save(member);
		this.memberRepository.flush();
		return result;
	}

	public void delete(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(member.getId() != 0);
		Assert.isTrue(this.memberRepository.exists(member.getId()));
		final int id = LoginService.getPrincipal().getId();
		final int memberid = member.getId();
		Assert.isTrue(id == memberid || LoginService.getPrincipal().getAuthorities().contains(Authority.ADMIN));

		this.memberRepository.delete(member);
	}

	public Collection<Member> findAll() {
		Collection<Member> result;

		result = this.memberRepository.findAll();

		return result;
	}

	public Member findOne(final int memberId) {
		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.memberRepository.flush();
	}

	//Other Business Methods

	public Member findByPrincipal() {
		Member result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Member findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Member result;

		result = this.memberRepository.findbyUserAccountID(userAccount.getId());

		return result;
	}

	public Collection<Member> findByPosition(final int positionId) {
		Assert.isTrue(positionId != 0);
		Collection<Member> result;

		result = this.memberRepository.findByPosition(positionId);

		Assert.notNull(result);

		return result;
	}

	public Collection<Member> findByBrotherhood(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);
		Collection<Member> result;

		result = this.memberRepository.findByBrotherhood(brotherhoodId);

		Assert.notNull(result);

		return result;
	}
}
