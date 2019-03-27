
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
import repositories.ChapterRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Box;
import domain.Chapter;
import domain.SocialProfile;
import forms.RegisterChapterForm;

@Service
@Transactional
public class ChapterService {

	// Managed Repository ----------------------------------------------------
	
	@Autowired
	private BoxService			boxService;
	
	@Autowired
	private BoxRepository		boxRepository;
	
	@Autowired
	private ChapterRepository	chapterRepository;

	// Constructor ----------------------------------------------------

	public ChapterService() {
		super();
	}

	public Chapter register(final RegisterChapterForm registerChapterForm) {
		final Chapter result = this.create();

		final UserAccount userAccount = new UserAccount();
		final String password = registerChapterForm.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		Assert.isTrue(registerChapterForm.getPassword().equals(registerChapterForm.getPassword2()));
		userAccount.setPassword(hashedPassword);
		userAccount.setUsername(registerChapterForm.getUsername());
		userAccount.setAccountNonLocked(true);

		//Seteo los atributos al resultado
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		final List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);
		result.setAddress(registerChapterForm.getAddress());
		result.setMiddleName(registerChapterForm.getMiddleName());
		result.setEmail(registerChapterForm.getEmail());
		result.setName(registerChapterForm.getName());
		result.setPhone(registerChapterForm.getPhone());
		result.setPhoto(registerChapterForm.getPhoto());
		result.setSurname(registerChapterForm.getSurname());
		result.setTitle(registerChapterForm.getTitle());
		result.setArea(null);
		
		this.save(result);

		return result;
	}
	
	
	// CRUD

	public Chapter create() {
		Chapter result;

		result = new Chapter();

		final Collection<Box> defaultBoxes = this.boxService.createDefaultBoxes();
		this.boxRepository.save(defaultBoxes);

		this.boxService.flush();
		final List<Box> allBoxes = new ArrayList<Box>(this.boxService.findAll());
		final List<Box> boxes = allBoxes.subList(allBoxes.size() - 5, allBoxes.size());
		result.setBoxes(boxes);

		result.setSpammer(false);
		result.setSocialProfiles(new HashSet<SocialProfile>());
		return result;
	}
	
	public Chapter save(final Chapter chapter) {
		Assert.notNull(chapter);
		
		Chapter result;
		result = this.chapterRepository.save(chapter);
		return result;
	}

	public void delete(final Chapter chapter) {
		Assert.notNull(chapter);
		Assert.isTrue(chapter.getId() != 0);
		Assert.isTrue(this.chapterRepository.exists(chapter.getId()));
		final int id = LoginService.getPrincipal().getId();
		final int chapterid = chapter.getId();
		Assert.isTrue(id == chapterid || LoginService.getPrincipal().getAuthorities().contains(Authority.ADMIN));

		this.chapterRepository.delete(chapter);
	}

	public Collection<Chapter> findAll() {
		Collection<Chapter> result;

		result = this.chapterRepository.findAll();

		return result;
	}

	public Chapter findOne(final int chapterId) {
		Chapter result;

		result = this.chapterRepository.findOne(chapterId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.chapterRepository.flush();
	}

	// Other Business Methods

	public Chapter findByPrincipal() {
		Chapter result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Chapter findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Chapter result;

		result = this.chapterRepository.findbyUserAccountID(userAccount.getId());

		return result;
	}
	
}
