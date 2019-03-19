
package controllers.brotherhood;

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

import services.BrotherhoodService;
import services.ParadeService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		Brotherhood brotherhood;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String date = sdf.format(new Date());

		brotherhood = this.brotherhoodService.findByPrincipal();
		requests = this.requestService.findByBrotherhood(brotherhood);

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("date", date);
		result.addObject("role", "brotherhood");
		result.addObject("requestURI", "request/brotherhood/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request;
		Parade parade;
		Collection<Parade> parades;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		parades = brotherhood.getParades();
		request = this.requestService.findOne(requestId);
		parade = this.paradeService.findOne(request.getParade().getId());
		Assert.isTrue(parades.contains(parade));

		result = new ModelAndView("request/display");
		result.addObject("request", request);
		result.addObject("role", "brotherhood");

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		Parade parade;
		Collection<Parade> parades;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		parades = brotherhood.getParades();
		request = this.requestService.findOne(requestId);
		parade = this.paradeService.findOne(request.getParade().getId());
		Assert.isTrue(parades.contains(parade));

		request = this.requestService.findOne(requestId);

		result = this.createEditModelAndView(request);
		return result;
	}

	// Accept --------------------------------------------------------

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);
		final Request accepted = this.requestService.acceptRequest(request);

		result = this.createEditModelAndView(accepted);
		result.addObject("action", "accept");
		return result;
	}

	// Reject --------------------------------------------------------

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);
		final Request rejected = this.requestService.rejectRequest(request);

		result = this.createEditModelAndView(rejected);
		result.addObject("action", "reject");
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

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		return this.createEditModelAndView(request, null);
	}

	protected ModelAndView createEditModelAndView(final Request request, final String message) {
		final ModelAndView result;

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("role", "brotherhood");
		result.addObject("message", message);
		result.addObject("requestURI", "request/brotherhood/edit.do");
		return result;
	}

}
