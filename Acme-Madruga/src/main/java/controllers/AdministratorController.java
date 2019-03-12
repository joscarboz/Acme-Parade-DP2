/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.FinderService;
import services.SystemConfigService;
import domain.Actor;
import domain.Area;
import domain.Brotherhood;
import domain.Member;
import domain.Position;
import domain.Parade;
import domain.SystemConfig;
import forms.RegisterAdminForm;
import forms.RegisterForm;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private AdministratorService adminService;

	@Autowired
	private FinderService finderService;

	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Dashboard
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {
		ModelAndView result;

		Double[] mpb = new Double[4];

		mpb = this.adminService.membersPerBrotherhoodStats();

		if (mpb == null) {
			mpb = new Double[4];
			mpb[0] = 0.;
			mpb[1] = 0.;
			mpb[2] = 0.;
			mpb[3] = 0.;

		}

		Collection<Brotherhood> largest = this.adminService
				.largestBrotherhoods();

		Collection<Brotherhood> smallest = this.adminService
				.smallestBrotherhoods();

		Double pending = this.adminService.pendingRequestsRatio();

		if (pending == null)
			pending = 0.;

		Double accepted = this.adminService.acceptedRequestsRatio();

		if (accepted == null)
			accepted = 0.;

		Double rejected = this.adminService.rejectedRequestsRatio();

		if (rejected == null)
			rejected = 0.;

		Collection<Parade> parades = this.adminService
				.upcomingParades();

		Collection<Member> acceptedReqMem = this.adminService
				.acceptedRequestMembers();

		Map<Position, Long> histogram = this.adminService.positionHistogram();

		Map<Area, Double[]> brotherhoodsPArea = this.adminService
				.brotherhoodsPerArea();

		Double[] sta = new Double[4];

		sta = this.adminService.brotherhoodsPerAreaStatistics();

		Double[] find = new Double[4];

		find = this.finderService.finderStatistics();

		if (find == null) {
			find = new Double[4];
			find[0] = 0.;
			find[1] = 0.;
			find[2] = 0.;
			find[3] = 0.;

		}
		Double emptyFinders = this.finderService.ratioEmptyFinders();

		if (emptyFinders == null)
			emptyFinders = 0.;

		result = new ModelAndView("administrator/dashboard");

		// MembersPerBrotherhood
		result.addObject("maximummpb", mpb[1]);
		result.addObject("minimummpb", mpb[2]);
		result.addObject("averagempb", mpb[0]);
		result.addObject("stdevmpb", mpb[3]);

		result.addObject("largest", largest);

		result.addObject("smallest", smallest);

		result.addObject("pending", pending);

		result.addObject("accepted", accepted);

		result.addObject("rejected", rejected);

		result.addObject("upcomProces", parades);

		result.addObject("acceptedReqMem", acceptedReqMem);

		result.addObject("histogram", histogram);

		result.addObject("brotpa", brotherhoodsPArea);

		// BrotherhoodsPerArea
		result.addObject("maximumbpa", sta[1]);
		result.addObject("minimumbpa", sta[0]);
		result.addObject("averagebpa", sta[2]);
		result.addObject("stdevbpa", sta[3]);

		// FinderStatistics
		result.addObject("maximumfs", find[1]);
		result.addObject("minimumfs", find[0]);
		result.addObject("averagefs", find[2]);
		result.addObject("stdevfs", find[3]);

		result.addObject("emptyf", emptyFinders);

		return result;

	}

	@RequestMapping(value = "/management", method = RequestMethod.GET)
	public ModelAndView userManagement() {
		ModelAndView result;

		Collection<Actor> actors;

		actors = this.actorService.findAll();

		result = new ModelAndView("administrator/management");

		result.addObject("actors", actors);

		return result;

	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int actorId) {

		ModelAndView result;

		final UserAccount uc = this.adminService.findByActor(actorId);

		final boolean banned = this.actorService.banActor(uc);

		result = new ModelAndView("redirect:management.do");

		if (!banned)
			result.addObject("showErrorBan", true);

		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final int actorId) {

		ModelAndView result;

		final UserAccount uc = this.adminService.findByActor(actorId);

		final boolean unbanned = this.actorService.unbanActor(uc);

		result = new ModelAndView("redirect:management.do");

		if (!unbanned)
			result.addObject("showErrorUnban", true);

		return result;
	}

	@RequestMapping(value = "/getSpammers")
	public ModelAndView getSpammers() {
		ModelAndView result;
		this.actorService.evaluateSpammers();
		result = new ModelAndView("redirect:management.do");

		return result;
	}

	@RequestMapping(value = "/getScore")
	public ModelAndView getScore() {
		ModelAndView result;
		this.actorService.scoreAllActors();
		result = new ModelAndView("redirect:management.do");

		return result;
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterAdminForm registerMemberForm,
			final BindingResult binding) {
		SystemConfig systemConfig;
		if (binding.hasErrors())
			return this.createRegisterModelAndView();
		else
			try {
				if (registerMemberForm.getPhone().matches("\\d{4,99}")) {
					systemConfig = this.systemConfigService
							.findSystemConfiguration();
					String newPhone = systemConfig.getPhonePrefix();
					newPhone += " " + registerMemberForm.getPhone();
					registerMemberForm.setPhone(newPhone);
				}
				this.adminService.register(registerMemberForm);
				return new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				return this.createRegisterModelAndView();
			}
	}

	protected ModelAndView createRegisterModelAndView() {
		final ModelAndView result = new ModelAndView("actor/register");
		final RegisterForm registerForm = new RegisterForm();
		final List<String> registerAuthorities = new ArrayList<String>();
		final List<String> adminAuthorities = new ArrayList<String>();
		adminAuthorities.add("ADMIN");

		registerAuthorities.add("MEMBER");
		registerAuthorities.add("BROTHERHOOD");

		result.addObject("registerForm", registerForm);
		result.addObject("adminAuthorities", adminAuthorities);
		result.addObject("registerAuthorities", registerAuthorities);
		return result;
	}
}
