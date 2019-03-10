
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
import services.ProcessionService;
import controllers.AbstractController;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping("/procession/member")
public class ProcessionMemberController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private MemberService		memberService;


	// Listing Processions from a Brotherhood

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Procession> processions;
		final Member member = this.memberService.findByPrincipal();
		processions = this.processionService.findAvailable(member);

		final Collection<Procession> res = new ArrayList<>();

		for (final Procession p : processions)
			if (p.isDraftMode() == false && p.getMoment().after(Calendar.getInstance().getTime()))
				res.add(p);

		result = new ModelAndView("procession/list");
		result.addObject("processions", res);
		result.addObject("requestURI", "procession/member/list.do");

		return result;
	}
}
