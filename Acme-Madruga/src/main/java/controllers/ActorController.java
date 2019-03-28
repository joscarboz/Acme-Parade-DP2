
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import repositories.MessageRepository;
import repositories.ParadeRepository;
import security.Authority;
import services.ActorService;
import services.AdministratorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ChapterService;
import services.MemberService;
import services.SponsorService;
import services.SystemConfigService;
import domain.Actor;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Message;
import domain.Parade;
import domain.SocialProfile;
import domain.SystemConfig;
import forms.EditBrotherhoodForm;
import forms.EditChapterForm;
import forms.RegisterAdminForm;
import forms.RegisterBrotherhoodForm;
import forms.RegisterChapterForm;
import forms.RegisterMemberForm;
import forms.SelectAreaForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Display ------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private SystemConfigService		systemConfigService;

	@Autowired
	private ParadeRepository		paradeRepository;

	@Autowired
	private MessageRepository		messageRepository;


	//-------------------------------------------------------

	public ActorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView list(final Integer actorId) {
		final ModelAndView result;
		Actor actor;
		result = new ModelAndView("actor/display");
		if (actorId != null) {
			actor = this.actorService.findOne(actorId);
			final Actor actor2 = this.actorService.findByPrincipal();
			result.addObject("actor2", actor2);
		} else
			actor = this.actorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = actor.getSocialProfiles();

		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", actor);
		result.addObject("requestURI", "actor/display.do");
		result.addObject("actorId", actor.getId());
		return result;
	}

	@RequestMapping(value = "/displayBrotherhood", method = RequestMethod.GET)
	public ModelAndView display(Integer actorId) {
		final ModelAndView result;
		Brotherhood brotherhood;
		result = new ModelAndView("actor/displayBrotherhood");
		if (actorId != null) {
			brotherhood = this.brotherhoodService.findOne(actorId);
			final Actor actor2 = this.actorService.findByPrincipal();
			result.addObject("actor2", actor2);
		} else
			actorId = this.actorService.findByPrincipal().getId();
		brotherhood = this.brotherhoodService.findOne(this.actorService.findByPrincipal().getId());

		final Collection<SocialProfile> socialProfiles = brotherhood.getSocialProfiles();
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", brotherhood);
		result.addObject("requestURI", "actor/displayBrotherhood.do");
		result.addObject("actorId", brotherhood.getId());
		return result;
	}

	@RequestMapping(value = "/displayChapter", method = RequestMethod.GET)
	public ModelAndView displayChapter(Integer actorId) {
		final ModelAndView result;
		Chapter chapter;
		result = new ModelAndView("actor/displayChapter");
		if (actorId != null) {
			chapter = this.chapterService.findOne(actorId);
			final Actor actor2 = this.actorService.findByPrincipal();
			result.addObject("actor2", actor2);
		} else
			actorId = this.actorService.findByPrincipal().getId();
		chapter = this.chapterService.findOne(this.actorService.findByPrincipal().getId());

		final Collection<SocialProfile> socialProfiles = chapter.getSocialProfiles();
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("actor", chapter);
		result.addObject("requestURI", "actor/displayChapter.do");
		result.addObject("actorId", chapter.getId());
		return result;
	}

	//-------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result;
		final Actor actor;

		actor = this.actorService.findByPrincipal();

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);
		result.addObject("requestURI", "actor/edit.do");

		return result;
	}

	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.GET)
	public ModelAndView editBrotherhood() {
		final ModelAndView result;
		final EditBrotherhoodForm editBrotherhoodForm = new EditBrotherhoodForm();
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(this.actorService.findByPrincipal().getId());

		editBrotherhoodForm.setAddress(brotherhood.getAddress());
		editBrotherhoodForm.setEmail(brotherhood.getEmail());
		editBrotherhoodForm.setMiddleName(brotherhood.getMiddleName());
		editBrotherhoodForm.setName(brotherhood.getName());
		editBrotherhoodForm.setPhone(brotherhood.getPhone());
		editBrotherhoodForm.setPhoto(brotherhood.getPhoto());
		editBrotherhoodForm.setSurname(brotherhood.getSurname());
		editBrotherhoodForm.setTitle(brotherhood.getTitle());
		editBrotherhoodForm.setEstablishment(brotherhood.getEstablishment());
		editBrotherhoodForm.setPictures(brotherhood.getPictures());

		result = new ModelAndView("actor/editBrotherhood");
		result.addObject("editBrotherhoodForm", editBrotherhoodForm);
		result.addObject("requestURI", "actor/editBrotherhood.do");

		return result;
	}

	@RequestMapping(value = "/editChapter", method = RequestMethod.GET)
	public ModelAndView editChapter() {
		final ModelAndView result;
		final EditChapterForm editChapterForm = new EditChapterForm();
		final Chapter chapter;

		chapter = this.chapterService.findOne(this.actorService.findByPrincipal().getId());

		editChapterForm.setAddress(chapter.getAddress());
		editChapterForm.setEmail(chapter.getEmail());
		editChapterForm.setMiddleName(chapter.getMiddleName());
		editChapterForm.setName(chapter.getName());
		editChapterForm.setPhone(chapter.getPhone());
		editChapterForm.setPhoto(chapter.getPhoto());
		editChapterForm.setSurname(chapter.getSurname());
		editChapterForm.setTitle(chapter.getTitle());

		result = new ModelAndView("actor/editChapter");
		result.addObject("editChapterForm", editChapterForm);
		result.addObject("requestURI", "actor/editChapter.do");

		return result;
	}

	//-------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Actor actor, final BindingResult binding) {
		ModelAndView result;
		Actor a;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actor);
		else
			try {

				a = this.actorService.findByPrincipal();
				a.setAddress(actor.getAddress());
				a.setEmail(actor.getEmail());
				a.setMiddleName(actor.getMiddleName());
				a.setName(actor.getName());
				a.setPhone(actor.getPhone());
				a.setPhoto(actor.getPhoto());
				a.setSurname(actor.getSurname());

				this.actorService.save(a);

				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actor, "actor.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@Valid final EditBrotherhoodForm editBrotherhoodForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood a;

		try {
			if (binding.hasErrors())
				result = this.createEditBrotherhoodModelAndView(editBrotherhoodForm, "actor.commit.error");
			else {
				a = this.brotherhoodService.findOne(this.actorService.findByPrincipal().getId());

				a.setAddress(editBrotherhoodForm.getAddress());
				a.setEmail(editBrotherhoodForm.getEmail());
				a.setMiddleName(editBrotherhoodForm.getMiddleName());
				a.setName(editBrotherhoodForm.getName());
				a.setPhone(editBrotherhoodForm.getPhone());
				a.setPhoto(editBrotherhoodForm.getPhoto());
				a.setSurname(editBrotherhoodForm.getSurname());
				a.setTitle(editBrotherhoodForm.getTitle());
				a.setEstablishment(editBrotherhoodForm.getEstablishment());
				a.setPictures(editBrotherhoodForm.getPictures());

				this.brotherhoodService.save(a);

				result = new ModelAndView("redirect:/actor/displayBrotherhood.do");
			}
		} catch (final Throwable oops) {
			result = this.createEditBrotherhoodModelAndView(editBrotherhoodForm, "actor.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/editChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView saveChapter(@Valid final EditChapterForm editChapterForm, final BindingResult binding) {
		ModelAndView result;
		Chapter a;

		try {
			if (binding.hasErrors())
				result = this.createEditChapterModelAndView(editChapterForm, "actor.commit.error");
			else {
				a = this.chapterService.findOne(this.actorService.findByPrincipal().getId());

				a.setAddress(editChapterForm.getAddress());
				a.setEmail(editChapterForm.getEmail());
				a.setMiddleName(editChapterForm.getMiddleName());
				a.setName(editChapterForm.getName());
				a.setPhone(editChapterForm.getPhone());
				a.setPhoto(editChapterForm.getPhoto());
				a.setSurname(editChapterForm.getSurname());
				a.setTitle(editChapterForm.getTitle());

				this.chapterService.save(a);

				result = new ModelAndView("redirect:/actor/displayChapter.do");
			}
		} catch (final Throwable oops) {
			result = this.createEditChapterModelAndView(editChapterForm, "actor.commit.error");
		}
		return result;
	}

	//-------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.createEditModelAndView(actor, null);

		return result;
	}

	protected ModelAndView createEditBrtoherhoodModelAndView(final EditBrotherhoodForm editBrotherhoodForm) {
		ModelAndView result;

		result = this.createEditBrotherhoodModelAndView(editBrotherhoodForm, null);

		return result;
	}

	protected ModelAndView createEditChapterModelAndView(final EditChapterForm editChapterForm) {
		ModelAndView result;

		result = this.createEditChapterModelAndView(editChapterForm, null);

		return result;
	}

	//-------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditBrotherhoodModelAndView(final EditBrotherhoodForm editBrotherhoodForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/editBrotherhood");

		result.addObject("editBrotherhoodForm", editBrotherhoodForm);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditChapterModelAndView(final EditChapterForm editChapterForm, final String messageCode) {
		ModelAndView result;
		final Collection<String> areaName = new ArrayList<>();

		result = new ModelAndView("actor/editChapter");

		result.addObject("areas", areaName);
		result.addObject("editChapterForm", editChapterForm);
		result.addObject("message", messageCode);

		return result;
	}

	//-------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		return this.createRegisterModelAndView();
	}

	@RequestMapping(value = "/registerBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		return this.createRegisterModelAndViewBrotherhood();
	}

	@RequestMapping(value = "/registerChapter", method = RequestMethod.GET)
	public ModelAndView createChapter() {
		return this.createRegisterModelAndViewChapter();
	}

	@RequestMapping(value = "/registerSponsor", method = RequestMethod.GET)
	public ModelAndView createSponsor() {
		return this.createRegisterModelAndViewSponsor();
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.GET)
	public ModelAndView createAdministrator() {
		return this.createRegisterModelAndViewAdministrator();
	}

	//-------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterMemberForm registerMemberForm, final BindingResult binding) {
		SystemConfig systemConfig;

		try {
			if (binding.hasErrors())
				return this.createRegisterModelAndView(registerMemberForm, "actor.commit.error");
			else if (registerMemberForm.getPhone().matches("\\d{4,99}")) {
				systemConfig = this.systemConfigService.findSystemConfiguration();
				String newPhone = systemConfig.getPhonePrefix();
				newPhone += " " + registerMemberForm.getPhone();
				registerMemberForm.setPhone(newPhone);
			}
			this.memberService.register(registerMemberForm);
			return new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			return this.createRegisterModelAndView(registerMemberForm, "actor.commit.error");

		}
	}

	@RequestMapping(value = "/registerBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterBrotherhoodForm registerBrotherhoodForm, final BindingResult binding) {
		SystemConfig systemConfig;

		try {
			if (binding.hasErrors())
				return this.createRegisterBrotherhoodModelAndView(registerBrotherhoodForm, "actor.commit.error");
			else if (registerBrotherhoodForm.getPhone().matches("\\d{4,99}")) {
				systemConfig = this.systemConfigService.findSystemConfiguration();
				String newPhone = systemConfig.getPhonePrefix();
				newPhone += " " + registerBrotherhoodForm.getPhone();
				registerBrotherhoodForm.setPhone(newPhone);
			}
			this.brotherhoodService.register(registerBrotherhoodForm);
			return new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			return this.createRegisterBrotherhoodModelAndView(registerBrotherhoodForm, "actor.commit.error");
		}
	}

	@RequestMapping(value = "/registerChapter", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterChapterForm registerChapterForm, final BindingResult binding) {
		SystemConfig systemConfig;

		try {
			if (binding.hasErrors())
				return this.createRegisterChapterModelAndView(registerChapterForm, "actor.commit.error");
			else if (registerChapterForm.getPhone().matches("\\d{4,99}")) {
				systemConfig = this.systemConfigService.findSystemConfiguration();
				String newPhone = systemConfig.getPhonePrefix();
				newPhone += " " + registerChapterForm.getPhone();
				registerChapterForm.setPhone(newPhone);
			}
			this.chapterService.register(registerChapterForm);
			return new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			return this.createRegisterChapterModelAndView(registerChapterForm, "actor.commit.error");

		}
	}

	@RequestMapping(value = "/registerSponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSponsor(@Valid final RegisterMemberForm registerMemberForm, final BindingResult binding) {
		SystemConfig systemConfig;

		try {
			if (binding.hasErrors())
				return this.createRegisterSponsorModelAndView(registerMemberForm, "actor.commit.error");
			else if (registerMemberForm.getPhone().matches("\\d{4,99}")) {
				systemConfig = this.systemConfigService.findSystemConfiguration();
				String newPhone = systemConfig.getPhonePrefix();
				newPhone += " " + registerMemberForm.getPhone();
				registerMemberForm.setPhone(newPhone);
			}
			this.sponsorService.register(registerMemberForm);
			return new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			return this.createRegisterSponsorModelAndView(registerMemberForm, "actor.commit.error");

		}
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterAdminForm registerAdminForm, final BindingResult binding) {
		SystemConfig systemConfig;

		try {
			if (binding.hasErrors())
				return this.createRegisterAdminModelAndView(registerAdminForm, "actor.commit.error");
			else if (registerAdminForm.getPhone().matches("\\d{4,99}")) {
				systemConfig = this.systemConfigService.findSystemConfiguration();
				String newPhone = systemConfig.getPhonePrefix();
				newPhone += " " + registerAdminForm.getPhone();
				registerAdminForm.setPhone(newPhone);
			}
			this.administratorService.register(registerAdminForm);
			return new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			return this.createRegisterAdminModelAndView(registerAdminForm, "actor.commit.error");
		}
	}

	//-------------------------------------------------------

	protected ModelAndView createRegisterModelAndView() {
		final ModelAndView result = new ModelAndView("actor/register");
		final RegisterMemberForm registerMemberForm = new RegisterMemberForm();

		result.addObject("registerMemberForm", registerMemberForm);

		return result;
	}

	protected ModelAndView createRegisterModelAndViewBrotherhood() {
		final ModelAndView result = new ModelAndView("actor/registerBrotherhood");
		final RegisterBrotherhoodForm registerBrotherhoodForm = new RegisterBrotherhoodForm();

		result.addObject("registerBrotherhoodForm", registerBrotherhoodForm);
		return result;
	}

	protected ModelAndView createRegisterModelAndViewChapter() {
		final ModelAndView result = new ModelAndView("actor/registerChapter");
		final RegisterChapterForm registerChapterForm = new RegisterChapterForm();

		result.addObject("registerChapterForm", registerChapterForm);
		return result;
	}

	protected ModelAndView createRegisterModelAndViewSponsor() {
		final ModelAndView result = new ModelAndView("actor/registerSponsor");
		final RegisterMemberForm registerMemberForm = new RegisterMemberForm();

		result.addObject("registerMemberForm", registerMemberForm);

		return result;
	}

	protected ModelAndView createRegisterModelAndViewAdministrator() {
		final ModelAndView result = new ModelAndView("actor/registerAdministrator");
		final RegisterAdminForm registerAdminForm = new RegisterAdminForm();

		result.addObject("registerAdminForm", registerAdminForm);

		return result;
	}

	//-------------------------------------------------------

	protected ModelAndView createRegisterModelAndView(final RegisterMemberForm registerMemberForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/register");
		result.addObject("registerMemberForm", registerMemberForm);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createRegisterBrotherhoodModelAndView(final RegisterBrotherhoodForm registerBrotherhoodForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerBrotherhood");
		result.addObject("registerBrotherhoodForm", registerBrotherhoodForm);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createRegisterChapterModelAndView(final RegisterChapterForm registerChapterForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerChapter");
		result.addObject("registerChapterForm", registerChapterForm);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createRegisterSponsorModelAndView(final RegisterMemberForm registerMemberForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerSponsor");
		result.addObject("registerMemberForm", registerMemberForm);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createRegisterAdminModelAndView(final RegisterAdminForm registerAdminForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerAdministrator");
		result.addObject("registerAdminForm", registerAdminForm);
		result.addObject("message", messageCode);

		return result;
	}

	//-------------------------------------------------------

	@RequestMapping(value = "/selectArea", method = RequestMethod.GET)
	public ModelAndView selectArea() {
		final ModelAndView result;
		final Collection<Chapter> chapters = this.chapterService.findAll();
		final Collection<Area> areas = this.areaService.findAll();
		final Collection<String> areasName = new ArrayList<>();
		final SelectAreaForm selectAreaForm = new SelectAreaForm();
		final Authority authB = new Authority();
		final Authority authC = new Authority();
		authB.setAuthority(Authority.BROTHERHOOD);
		authC.setAuthority(Authority.CHAPTER);
		result = new ModelAndView("actor/selectArea");

		if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authB)) {
			Assert.isTrue(this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea"));
			for (final Area a : areas)
				areasName.add(a.getName());
			areasName.remove("defaultArea");
			result.addObject("selectAreaForm", selectAreaForm);
			result.addObject("areas", areasName);
			result.addObject("requestURI", "actor/selectArea.do");
		}

		if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authC)) {
			Assert.isTrue(this.chapterService.findByPrincipal().getArea() == null);
			for (final Chapter chapter : chapters)
				areas.remove(chapter.getArea());
			for (final Area a : areas)
				areasName.add(a.getName());
			areasName.remove("defaultArea");
			result.addObject("selectAreaForm", selectAreaForm);
			result.addObject("areas", areasName);
			result.addObject("requestURI", "actor/selectArea.do");
		}

		return result;
	}

	@RequestMapping(value = "/selectArea", method = RequestMethod.POST, params = "save")
	public ModelAndView saveArea(@Valid final SelectAreaForm selectAreaForm, final BindingResult binding) {
		ModelAndView result;
		final Authority authB = new Authority();
		final Authority authC = new Authority();
		authB.setAuthority(Authority.BROTHERHOOD);
		authC.setAuthority(Authority.CHAPTER);

		try {
			if (binding.hasErrors())
				result = this.createSelectAreaModelAndView("actor.commit.error");
			else {
				if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authB)) {
					Assert.isTrue(this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea"));
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					brotherhood.setArea((this.areaService.findByName(selectAreaForm.getAreas().iterator().next()).iterator().next()));
					this.brotherhoodService.save(brotherhood);
				}

				if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authC)) {
					Assert.isTrue(this.chapterService.findByPrincipal().getArea() == null);
					final Chapter chapter = this.chapterService.findByPrincipal();
					chapter.setArea((this.areaService.findByName(selectAreaForm.getAreas().iterator().next()).iterator().next()));
					this.chapterService.save(chapter);
				}

				result = new ModelAndView("redirect:/");
			}
		} catch (final Throwable oops) {
			result = this.createSelectAreaModelAndView("actor.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ModelAndView account() {
		return new ModelAndView("actor/account");
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public ModelAndView deleteUser() {
		final Authority authB = new Authority();
		authB.setAuthority(Authority.BROTHERHOOD);
		final Authority authC = new Authority();
		authC.setAuthority(Authority.CHAPTER);
		final Authority authM = new Authority();
		authM.setAuthority(Authority.MEMBER);
		final Collection<Message> messages = this.messageRepository.findbySender(this.actorService.findByPrincipal().getId());
		for (final Message message : messages)
			this.messageRepository.delete(message);
		if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authC)) {
			final Chapter chapter = this.chapterService.findByPrincipal();
			chapter.setArea(null);
			this.chapterService.save(chapter);
			this.actorService.deleteActor(chapter.getId());
		}
		if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authB)) {
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			final Collection<Parade> parades = brotherhood.getParades();
			for (final Parade parade : parades) {
				parade.setDraftMode(true);
				this.paradeRepository.save(parade);
			}
			this.brotherhoodService.deleteBrotherhood(brotherhood.getId());

		} else
			this.actorService.deleteActor(this.actorService.findByPrincipal().getId());

		return new ModelAndView("redirect:/j_spring_security_logout");

	}
	@RequestMapping(value = "/getPersonalData", method = RequestMethod.GET)
	public ModelAndView getPersonalData() {
		ModelAndView result;
		final Authority authB = new Authority();
		authB.setAuthority(Authority.BROTHERHOOD);
		final List<Object> data = new ArrayList<Object>();
		if (!this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authB)) {
			final Actor actor = this.actorService.findByPrincipal();
			data.add(actor.getName());
			data.add(actor.getMiddleName());
			data.add(actor.getSurname());
			data.add(actor.getPhoto());
			data.add(actor.getEmail());
			data.add(actor.getPhone());
			data.add(actor.getAddress());

		} else {

			final Brotherhood actor = this.brotherhoodService.findByPrincipal();
			data.add(actor.getName());
			data.add(actor.getMiddleName());
			data.add(actor.getSurname());
			data.add(actor.getPhoto());
			data.add(actor.getEmail());
			data.add(actor.getPhone());
			data.add(actor.getAddress());
			data.add(actor.getTitle());
			data.add(actor.getEstablishment());
			data.add(actor.getPictures());
		}
		result = new ModelAndView("actor/getPersonalData");
		result.addObject("data", data);
		return result;

	}
	protected ModelAndView createSelectAreaModelAndView(final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/selectArea");

		result.addObject("message", messageCode);

		return result;
	}

}
