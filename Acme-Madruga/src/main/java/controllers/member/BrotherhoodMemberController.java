
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.MemberService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping("/brotherhood/member")
public class BrotherhoodMemberController extends AbstractController {

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Constructor

	public BrotherhoodMemberController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Brotherhood> brotherhoods;
		Member member;

		member = this.memberService.findByPrincipal();
		brotherhoods = this.brotherhoodService.findByMember(member.getId());
		result = new ModelAndView("brotherhood/list");
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("requestURI", "brotherhood/member/list.do");

		return result;
	}
}
