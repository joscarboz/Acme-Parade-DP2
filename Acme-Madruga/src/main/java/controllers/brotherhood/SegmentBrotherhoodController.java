
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Parade;
import domain.Segment;
import forms.SegmentForm;

@Controller
@RequestMapping("/segment/brotherhood")
public class SegmentBrotherhoodController extends AbstractController {

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int paradeId) {
		ModelAndView result;

		final Parade parade = this.paradeService.findOne(paradeId);
		result = new ModelAndView("segment/list");
		final Collection<Segment> segments = parade.getSegments();
		final boolean isParadeEmpty = segments.isEmpty();
		result.addObject("parade", parade);
		result.addObject("segments", segments);
		result.addObject("isParadeEmpty", isParadeEmpty);
		result.addObject("requestURI", "segment/brotherhood/list.do");
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int segmentId) {
		ModelAndView result;
		final Segment segment = this.segmentService.findOne(segmentId);
		result = new ModelAndView("segment/display");
		result.addObject("segment", segment);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		final ModelAndView result;

		final Parade parade = this.paradeService.findOne(paradeId);
		final Segment segment = this.segmentService.create(paradeId);
		final SegmentForm segmentForm = this.segmentService.toSegmentForm(segment, parade);

		result = this.createEditModelAndView(parade, segmentForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "segmentId") final int segmentId, @RequestParam(value = "paradeId") final int paradeId) {
		final ModelAndView result;

		final Parade parade = this.paradeService.findOne(paradeId);
		final Segment segment = this.segmentService.findOne(segmentId);
		final SegmentForm segmentForm = this.segmentService.toSegmentForm(segment, parade);

		result = this.createEditModelAndView(parade, segmentForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(@Valid final SegmentForm segmentForm, final BindingResult binding) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(segmentForm.getParadeId());
		if (binding.hasErrors())
			result = this.createEditModelAndView(parade, segmentForm);
		else
			try {
				final Segment segment = this.segmentService.reconstruct(segmentForm);
				this.segmentService.save(segment, parade);
				result = new ModelAndView("redirect:list.do?paradeId=" + parade.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(parade, segmentForm, "segment.commit.error");

			}
		return result;
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int paradeId) {
		ModelAndView result;
		this.segmentService.deleteLastSegmentFromParade(paradeId);
		result = new ModelAndView("redirect:list.do?paradeId=" + paradeId);

		return result;
	}

	@RequestMapping(value = "clear", method = RequestMethod.GET)
	public ModelAndView clear(@RequestParam final int paradeId) {
		ModelAndView result;
		this.segmentService.deleteAllFromParade(paradeId);
		result = new ModelAndView("redirect:list.do?paradeId=" + paradeId);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final SegmentForm segmentForm) {
		return this.createEditModelAndView(parade, segmentForm, null);
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final SegmentForm segmentForm, final String message) {
		ModelAndView result;
		final Segment segment = this.segmentService.reconstruct(segmentForm);
		final boolean isParadeEmpty = parade.getSegments().isEmpty();
		final boolean isLastSegment = this.segmentService.findLastSegmentFromParade(parade.getId()).equals(segment);
		result = new ModelAndView("segment/edit");
		result.addObject("message", message);
		result.addObject("segmentForm", segmentForm);
		result.addObject("isParadeEmpty", isParadeEmpty);
		result.addObject("isLastSegment", isLastSegment);
		return result;
	}

}
