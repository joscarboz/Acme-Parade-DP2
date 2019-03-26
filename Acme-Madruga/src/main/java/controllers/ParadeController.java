
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import services.SponsorshipService;
import services.SystemConfigService;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private SystemConfigService	systemConfigService;


	//Constructor

	public ParadeController() {
		super();
	}

	// Listing Parades from a Brotherhood

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId) {
		ModelAndView result;
		Collection<Parade> parades;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		parades = brotherhood.getParades();
		final Collection<Parade> res = new ArrayList<>();

		for (final Parade p : parades)
			if (p.isDraftMode() == false && p.getStatus().equals("accepted"))
				res.add(p);

		result = new ModelAndView("parade/list");
		result.addObject("parades", res);
		result.addObject("requestURI", "parades/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		ModelAndView result;
		final Parade parade;
		Collection<Sponsorship> sponsorships;
		Random rnd;
		Sponsorship sponsorship;
		String banner;

		parade = this.paradeService.findOne(paradeId);
		final Collection<Float> floats = parade.getFloats();
		sponsorships = this.sponsorshipService.findByParadeId(paradeId);

		result = new ModelAndView("parade/display");
		if (sponsorships.size() > 0) {
			rnd = new Random();
			sponsorship = (Sponsorship) sponsorships.toArray()[rnd.nextInt(sponsorships.size())];
			sponsorship.setFare(sponsorship.getFare() + this.systemConfigService.findSystemConfiguration().getFareCharge());
			banner = sponsorship.getBanner();
			result.addObject("banner", banner);
			result.addObject("bannerLink", sponsorship.getTargetUrl());

		}

		result.addObject("floats", floats);
		result.addObject("parade", parade);
		result.addObject("role", "none");

		return result;
	}
}
