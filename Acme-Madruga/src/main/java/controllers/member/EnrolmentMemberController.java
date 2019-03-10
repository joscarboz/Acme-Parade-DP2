
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class EnrolmentMemberController {

	@Autowired
	private MemberService		memberService;
	@Autowired
	private EnrolmentService	enrolmentService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Enrolment> enrolments;
		final Collection<Enrolment> pastEnrolments;
		Member member;

		member = this.memberService.findByPrincipal();
		enrolments = this.enrolmentService.getActiveEnrolments(member);
		pastEnrolments = this.enrolmentService.getDropOutMemberEnrolments(member);

		result = new ModelAndView("enrolment/list");
		result.addObject("enrolments", enrolments);
		result.addObject("pastEnrolments", pastEnrolments);
		result.addObject("requestURI", "enrolment/member/list.do");

		return result;
	}

	// Drop Out --------------------------------------------------------

	@RequestMapping(value = "/dropOut", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int enrolmentId) {
		ModelAndView result;
		Enrolment enrolment;
		Member member;
		member = this.memberService.findByPrincipal();
		enrolment = this.enrolmentService.findOne(enrolmentId);

		try {

			enrolment = this.enrolmentService.findOne(enrolmentId);
			Assert.isTrue(member.getId() == enrolment.getMember().getId());
			this.enrolmentService.dropOut(enrolment);
			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(enrolment, "enrolment.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/enrol", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		try {
			final Enrolment enrolment = this.enrolmentService.create(brotherhood);
			this.enrolmentService.save(enrolment);
			result = new ModelAndView("redirect:/#");
		} catch (final Throwable oops) {
			result = new ModelAndView("brotherhood/list");
			final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findAll();
			result.addObject("brotherhoods", brotherhoods);
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		return this.createEditModelAndView(enrolment, null);
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String message) {
		final ModelAndView result;

		result = new ModelAndView("enrolment/list");
		result.addObject("role", "member");
		result.addObject("enrolment", enrolment);
		result.addObject("message", message);
		result.addObject("requestURI", "enrolment/member/list.do");
		return result;
	}
}
