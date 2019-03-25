
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ParadeService		paradeService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Sponsorship> sponsorships;

		final Sponsor principal = this.sponsorService.findByPrincipal();

		sponsorships = principal.getSponsorships();

		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		final ModelAndView result;
		Sponsorship res;
		Collection<Sponsorship> sponsorships;
		Sponsor sponsor;

		sponsor = this.sponsorService.findByPrincipal();
		sponsorships = sponsor.getSponsorships();
		res = this.sponsorshipService.findOne(sponsorshipId);
		Assert.isTrue(sponsorships.contains(res));
		result = new ModelAndView("sponsorship/display");
		result.addObject("res", res);

		return result;
	}

	//Create ----

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		final SponsorshipForm sponsorshipForm = new SponsorshipForm();
		final Sponsorship sponsorship = new Sponsorship();
		final CreditCard creditCard = new CreditCard();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("paradeId", paradeId);
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("sponsorship", sponsorship);
		result.addObject("creditCard", creditCard);
		result.addObject("requestURI", "sponsorship/sponsor/create.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SponsorshipForm sponsorshipForm, final BindingResult binding, @RequestParam final int tutorialId) {
		ModelAndView result;
		final Sponsorship sponsorship = new Sponsorship();
		final CreditCard creditCard = new CreditCard();

		creditCard.setMake(sponsorshipForm.getMake());
		creditCard.setCvv(sponsorshipForm.getCvv());
		creditCard.setExpirationMonth(sponsorshipForm.getExpirationMonth());
		creditCard.setExpirationYear(sponsorshipForm.getExpirationMonth());
		creditCard.setHolder(sponsorshipForm.getHolder());
		creditCard.setNumber(sponsorshipForm.getNumber());

		sponsorship.setBanner(sponsorshipForm.getBanner());
		sponsorship.setTargetUrl(sponsorshipForm.getTargetUrl());
		//sponsorship.setSponsor((Sponsor) this.actorService.findByPrincipal());
		sponsorship.setParade(this.paradeService.findOne(tutorialId));

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorshipForm, tutorialId, creditCard, sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship, creditCard);
				result = new ModelAndView("redirect:/tutorial/sponsor/list.do?");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(sponsorshipForm, tutorialId, creditCard, sponsorship, "sponsorshipForm.commit.error");
				result.addObject("tutorialId", tutorialId);
				result.addObject("sponsorshipForm", sponsorshipForm);
				result.addObject("sponsorship", sponsorship);
				result.addObject("creditCard", creditCard);
				oops.printStackTrace();
			}

		return result;
	}

	// Ancillary Methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final int paradeId, final CreditCard creditCard, final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorshipForm, paradeId, creditCard, sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final int paradeId, final CreditCard creditCard, final Sponsorship sponsorship, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("paradeId", paradeId);
		result.addObject("creditCard", creditCard);
		result.addObject("sponsorship", sponsorship);
		result.addObject("message", messageCode);

		return result;
	}
}
