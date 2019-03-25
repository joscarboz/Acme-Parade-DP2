
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
import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Box;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;
import forms.RegisterMemberForm;

@Service
@Transactional
public class SponsorService {

	// Managed Repository ----------------------------------------------------
	
	@Autowired
	private BoxService			boxService;
	
	@Autowired
	private BoxRepository		boxRepository;
	
	@Autowired
	private SponsorRepository	sponsorRepository;


	// Constructor ----------------------------------------------------

	public SponsorService() {
		super();
	}

	public Sponsor register(final RegisterMemberForm registerMemberForm) {
		final Sponsor result = this.create();

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
		authority.setAuthority(Authority.SPONSOR);
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
	
	
	// CRUD

	public Sponsor create() {
		Sponsor result;

		result = new Sponsor();

		final Collection<Box> defaultBoxes = this.boxService.createDefaultBoxes();
		this.boxRepository.save(defaultBoxes);

		this.boxService.flush();
		final List<Box> allBoxes = new ArrayList<Box>(this.boxService.findAll());
		final List<Box> boxes = allBoxes.subList(allBoxes.size() - 5, allBoxes.size());
		result.setBoxes(boxes);

		result.setSpammer(false);
		result.setSocialProfiles(new HashSet<SocialProfile>());
		result.setSponsorships(new HashSet<Sponsorship>());
		return result;
	}
	
	public Sponsor save(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Sponsor result;
		result = this.sponsorRepository.save(sponsor);
		return result;
	}

	public void delete(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() != 0);
		Assert.isTrue(this.sponsorRepository.exists(sponsor.getId()));
		final int id = LoginService.getPrincipal().getId();
		final int sponsorid = sponsor.getId();
		Assert.isTrue(id == sponsorid || LoginService.getPrincipal().getAuthorities().contains(Authority.ADMIN));

		this.sponsorRepository.delete(sponsor);
	}

	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;

		result = this.sponsorRepository.findAll();

		return result;
	}

	public Sponsor findOne(final int sponsorId) {
		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

	// Other Business Methods

	public Sponsor findByPrincipal() {
		Sponsor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Sponsor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Sponsor result;

		result = this.sponsorRepository.findbyUserAccountID(userAccount.getId());

		return result;
	}
	
}
