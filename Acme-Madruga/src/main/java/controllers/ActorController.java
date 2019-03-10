
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.AreaService;
import services.BrotherhoodService;
import services.MemberService;
import services.SystemConfigService;
import domain.Actor;
import domain.Area;
import domain.Brotherhood;
import domain.SocialProfile;
import domain.SystemConfig;
import forms.EditBrotherhoodForm;
import forms.RegisterAdminForm;
import forms.RegisterBrotherhoodForm;
import forms.RegisterMemberForm;

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
	private AdministratorService	administratorService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private SystemConfigService		systemConfigService;


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
		final Collection<Area> areas;
		final Collection<String> areaName = new ArrayList<>();

		brotherhood = this.brotherhoodService.findOne(this.actorService.findByPrincipal().getId());

		if (brotherhood.getArea().getName().equals("defaultArea")) {
			areas = this.areaService.findAll();
			for (final Area area : areas)
				areaName.add(area.getName());
		}

		editBrotherhoodForm.setArea(brotherhood.getArea());
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
		result.addObject("areas", areaName);
		result.addObject("requestURI", "actor/editBrotherhood.do");

		return result;
	}

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

				if (a.getArea().getName().equals("defaultArea") && editBrotherhoodForm.getAreas() != null)
					a.setArea(this.areaService.findByName(editBrotherhoodForm.getAreas().iterator().next()).iterator().next());

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

	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditBrotherhoodModelAndView(final EditBrotherhoodForm editBrotherhoodForm, final String messageCode) {
		ModelAndView result;
		final Collection<Area> areas;
		final Collection<String> areaName = new ArrayList<>();

		result = new ModelAndView("actor/editBrotherhood");
		if (editBrotherhoodForm.getArea().getName().equals("defaultArea")) {
			areas = this.areaService.findAll();
			for (final Area area : areas)
				areaName.add(area.getName());
		}

		result.addObject("areas", areaName);
		result.addObject("editBrotherhoodForm", editBrotherhoodForm);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		return this.createRegisterModelAndView();
	}

	@RequestMapping(value = "/registerBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		return this.createRegisterModelAndViewBrotherhood();
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.GET)
	public ModelAndView createAdministrator() {
		return this.createRegisterModelAndViewAdministrator();
	}

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

	protected ModelAndView createRegisterModelAndViewAdministrator() {
		final ModelAndView result = new ModelAndView("actor/registerAdministrator");
		final RegisterAdminForm registerAdminForm = new RegisterAdminForm();

		result.addObject("registerAdminForm", registerAdminForm);

		return result;
	}

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

	protected ModelAndView createRegisterAdminModelAndView(final RegisterAdminForm registerAdminForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerAdministrator");
		result.addObject("registerAdminForm", registerAdminForm);
		result.addObject("message", messageCode);

		return result;
	}
}
