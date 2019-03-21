
package controllers.member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping("/parade/member")
public class ParadeMemberController extends AbstractController {

	@Autowired
	private ParadeService	paradeService;

	@Autowired
	private MemberService	memberService;


	// Listing Parades from a Brotherhood

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Parade> parades;
		final Member member = this.memberService.findByPrincipal();
		parades = this.paradeService.findAvailable(member);

		final Collection<Parade> res = new ArrayList<>();

		for (final Parade p : parades)
			if (p.isDraftMode() == false && p.getMoment().after(Calendar.getInstance().getTime()) && p.getStatus().equals("accepted"))
				res.add(p);

		result = new ModelAndView("parade/list");
		result.addObject("parades", res);
		result.addObject("requestURI", "parade/member/list.do");

		return result;
	}
}
