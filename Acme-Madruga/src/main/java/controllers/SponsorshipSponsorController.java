
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
import services.SystemConfigService;
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

	@Autowired
	private SystemConfigService	systemConfigService;


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
		result.addObject("sponsorship", res);

		return result;
	}

	//Create ----

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		final ModelAndView result;
		final SponsorshipForm sponsorshipForm = new SponsorshipForm();
		final Sponsorship sponsorship = new Sponsorship();
		final CreditCard creditCard = new CreditCard();

		result = this.createEditModelAndView(sponsorshipForm, paradeId, creditCard, sponsorship);

		return result;
	}

	@RequestMapping(value = "/disable", method = RequestMethod.GET)
	public ModelAndView disable(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		this.sponsorshipService.disable(sponsorshipId);
		result = new ModelAndView("redirect:display.do?sponsorshipId=" + sponsorshipId);
		return result;
	}

	@RequestMapping(value = "/enable", method = RequestMethod.GET)
	public ModelAndView enable(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		this.sponsorshipService.enable(sponsorshipId);
		result = new ModelAndView("redirect:display.do?sponsorshipId=" + sponsorshipId);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		final ModelAndView result;
		final SponsorshipForm sponsorshipForm = new SponsorshipForm();
		final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		final CreditCard creditCard = sponsorship.getCreditCard();
		final int paradeId = sponsorship.getParade().getId();
		sponsorshipForm.setBanner(sponsorship.getBanner());
		sponsorshipForm.setCvv(creditCard.getCvv());
		sponsorshipForm.setExpirationMonth(creditCard.getExpirationMonth());
		sponsorshipForm.setExpirationYear(creditCard.getExpirationYear());
		sponsorshipForm.setHolder(creditCard.getHolder());
		sponsorshipForm.setId(sponsorship.getId());
		sponsorshipForm.setMake(creditCard.getMake());
		sponsorshipForm.setNumber(creditCard.getNumber());
		sponsorshipForm.setParade(sponsorship.getParade());
		sponsorshipForm.setTargetUrl(sponsorship.getTargetUrl());
		sponsorshipForm.setVersion(sponsorship.getVersion());

		result = this.createEditModelAndView(sponsorshipForm, paradeId, creditCard, sponsorship);

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;
		Sponsorship sponsorship = new Sponsorship();
		final CreditCard creditCard = new CreditCard();

		if (sponsorshipForm.getId() != 0) {
			sponsorship = this.sponsorshipService.findOne(sponsorshipForm.getId());
			creditCard.setId(sponsorship.getCreditCard().getId());
			creditCard.setVersion(sponsorship.getCreditCard().getVersion());
		}

		creditCard.setMake(sponsorshipForm.getMake());
		creditCard.setCvv(sponsorshipForm.getCvv());
		creditCard.setExpirationMonth(sponsorshipForm.getExpirationMonth());
		creditCard.setExpirationYear(sponsorshipForm.getExpirationYear());
		creditCard.setHolder(sponsorshipForm.getHolder());
		creditCard.setNumber(sponsorshipForm.getNumber());
		sponsorship.setId(sponsorshipForm.getId());
		sponsorship.setBanner(sponsorshipForm.getBanner());
		sponsorship.setTargetUrl(sponsorshipForm.getTargetUrl());
		//sponsorship.setSponsor((Sponsor) this.actorService.findByPrincipal());
		final int paradeId = sponsorshipForm.getParade().getId();
		sponsorship.setParade(this.paradeService.findOne(paradeId));

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorshipForm, paradeId, creditCard, sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship, creditCard);
				result = new ModelAndView("redirect:/sponsorship/sponsor/list.do?");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(sponsorshipForm, paradeId, creditCard, sponsorship, "sponsorshipForm.commit.error");
				result.addObject("paradeId", paradeId);
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

		final Collection<String> creditCardMakes = this.systemConfigService.findSystemConfiguration().getCreditCardMakes();
		result = new ModelAndView("sponsorship/edit");
		sponsorshipForm.setParade(this.paradeService.findOne(paradeId));
		result.addObject("creditCardMakes", creditCardMakes);
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("creditCard", creditCard);
		result.addObject("sponsorship", sponsorship);
		result.addObject("message", messageCode);

		return result;
	}
}
