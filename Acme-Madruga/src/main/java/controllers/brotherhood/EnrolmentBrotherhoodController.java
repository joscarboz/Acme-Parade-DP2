
package controllers.brotherhood;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.PositionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Position;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class EnrolmentBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private Validator			validator;


	//Constructor

	public EnrolmentBrotherhoodController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Brotherhood brotherhood;
		Collection<Enrolment> members;
		Collection<Enrolment> enrolments;

		brotherhood = this.brotherhoodService.findByPrincipal();
		members = new HashSet<Enrolment>(brotherhood.getEnrolments());
		members.removeAll(this.enrolmentService.getDropOutBrotherhoodEnrolments(brotherhood));
		members.removeAll(this.enrolmentService.getPendingBrotherhoodEnrolments(brotherhood));

		enrolments = new HashSet<Enrolment>(brotherhood.getEnrolments());
		enrolments.removeAll(this.enrolmentService.getDropOutBrotherhoodEnrolments(brotherhood));
		enrolments.retainAll(this.enrolmentService.getPendingBrotherhoodEnrolments(brotherhood));
		final Collection<Enrolment> pastEnrolments = this.enrolmentService.getPendingBrotherhoodEnrolments(brotherhood);
		result = new ModelAndView("enrolment/brotherhood/list");
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");
		result.addObject("hasArea", hasArea);
		result.addObject("members", members);
		result.addObject("enrolments", enrolments);
		result.addObject("pastEnrolments", pastEnrolments);
		result.addObject("requestURI", "enrolment/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/dropOut", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int enrolmentId) {
		ModelAndView result;
		Enrolment enrolment;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		enrolment = this.enrolmentService.findOne(enrolmentId);

		try {

			enrolment = this.enrolmentService.findOne(enrolmentId);
			Assert.isTrue(brotherhood.getEnrolments().contains(enrolment));
			this.enrolmentService.dropOut(enrolment);
			result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(enrolment, "enrolment.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int enrolmentId) {
		ModelAndView result;
		Enrolment enrolment;

		enrolment = this.enrolmentService.findOne(enrolmentId);
		final Collection<Position> position = this.positionService.findAll();

		result = new ModelAndView("enrolment/brotherhood/accept");
		result.addObject("position", position);
		result.addObject("enrolment", enrolment);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/accept", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Enrolment enrolment, final BindingResult binding) {
		ModelAndView result;
		Enrolment enrolment2 = new Enrolment();
		enrolment2 = this.enrolmentService.reconstruct(enrolment, binding);
		this.validator.validate(enrolment2, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(enrolment2);
		else
			try {
				this.enrolmentService.save(enrolment2);

				result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(enrolment2, "enrolment.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		return this.createEditModelAndView(enrolment, null);
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String message) {
		final ModelAndView result;
		final Collection<Position> position = this.positionService.findAll();
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");
		result = new ModelAndView("enrolment/brotherhood/list");
		result.addObject("enrolment", enrolment);
		result.addObject("hasArea", hasArea);
		result.addObject("position", position);
		result.addObject("message", message);
		result.addObject("requestURI", "enrolment/brotherhood/list.do");
		return result;
	}
}
