
package controllers;

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
import domain.Member;
import domain.SocialProfile;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController {

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Constructor

	public MemberController() {
		super();
	}

	//Listing for all users

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood;
		Collection<Member> members;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		members = this.memberService.findByBrotherhood(brotherhood.getId());

		result = new ModelAndView("member/list");
		result.addObject("member", members);
		result.addObject("requestURI", "member/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int memberId) {
		ModelAndView result;
		final Member member;

		member = this.memberService.findOne(memberId);
		result = new ModelAndView("member/display");
		final Collection<SocialProfile> socialProfiles = member.getSocialProfiles();

		result.addObject("socialProfiles", socialProfiles);

		result.addObject("member", member);

		return result;
	}
}
