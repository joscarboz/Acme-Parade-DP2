
package controllers.member;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import services.ParadeService;
import services.RequestService;
import controllers.AbstractController;
import domain.Member;
import domain.Parade;
import domain.Request;

@Controller
@RequestMapping("/request/member")
public class RequestMemberController extends AbstractController {

	@Autowired
	private RequestService	requestService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private ParadeService	paradeService;


	//Constructor

	public RequestMemberController() {
		super();
	}

	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		Member member;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String date = sdf.format(new Date());

		member = this.memberService.findByPrincipal();
		requests = this.requestService.findByMember(member);

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("date", date);
		result.addObject("role", "member");
		result.addObject("requestURI", "request/member/list.do");

		return result;
	}

	// Creation --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Integer paradeId) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		final Member member = this.memberService.findByPrincipal();

		final Request request = this.requestService.create();
		request.setMember(member);
		request.setParade(parade);

		try {
			this.requestService.save(request);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(request, "request.commit.error");
		}

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		Member member;

		member = this.memberService.findByPrincipal();
		request = this.requestService.findOne(requestId);
		Assert.isTrue(member.getId() == request.getMember().getId());
		result = new ModelAndView("request/display");
		result.addObject("request", request);
		result.addObject("role", "member");

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);

		result = this.createEditModelAndView(request);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Request request, final BindingResult binding) {
		ModelAndView result;

		request = this.requestService.reconstruct(request, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(request);
		else
			try {
				this.requestService.save(request);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		return result;
	}

	// Delete --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request = this.requestService.findOne(requestId);

		try {
			this.requestService.delete(request);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(request, "request.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		return this.createEditModelAndView(request, null);
	}

	protected ModelAndView createEditModelAndView(final Request request, final String message) {
		final ModelAndView result;

		final Collection<Parade> parades = this.paradeService.findAll();

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("role", "member");
		result.addObject("parades", parades);
		result.addObject("message", message);
		result.addObject("requestURI", "request/member/edit.do");
		return result;
	}

}
