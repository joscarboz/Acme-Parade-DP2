
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.MemberService;
import domain.Brotherhood;
import domain.History;
import domain.Member;
import domain.Parade;
import domain.SocialProfile;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;


	//Constructor

	public BrotherhoodController() {
		super();
	}

	//Listing for all users

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAll();

		result = new ModelAndView("brotherhood/list");
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		final Brotherhood brotherhood;
		final Collection<Member> members;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		members = this.memberService.findByBrotherhood(brotherhoodId);

		final Collection<Parade> res = new ArrayList<>();

		for (final Parade p : brotherhood.getParades())
			if (p.isDraftMode() == false && p.getMoment().after(Calendar.getInstance().getTime()))
				res.add(p);

		result = new ModelAndView("brotherhood/display");
		final Collection<SocialProfile> socialProfiles = brotherhood.getSocialProfiles();
		final History history = brotherhood.getHistory();
		result.addObject("socialProfiles", socialProfiles);

		result.addObject("brotherhood", brotherhood);
		result.addObject("history", history);
		result.addObject("floats", brotherhood.getFloats());
		result.addObject("parades", res);
		result.addObject("members", members);
		return result;
	}
}
