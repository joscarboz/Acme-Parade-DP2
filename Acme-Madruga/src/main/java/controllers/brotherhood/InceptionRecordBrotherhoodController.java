
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.InceptionRecord;

@Controller
@RequestMapping("/history/inceptionRecord")
public class InceptionRecordBrotherhoodController extends AbstractController {

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		InceptionRecord res;

		res = this.inceptionRecordService.create();

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("inceptionRecord", res);
		result.addObject("id", res.getId());
		result.addObject("requestURI", "history/inceptionRecord/create.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		InceptionRecord res;

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		res = brotherhood.getHistory().getInceptionRecord();

		result = this.createEditModelAndView(res);
		result.addObject("requestURI", "history/inceptionRecord/edit.do");
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("inceptionRecord") @Valid final InceptionRecord res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.inceptionRecordService.save(res);
				result = new ModelAndView("history/display");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "record.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final InceptionRecord res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("inceptionRecord", res);
		result.addObject("id", res.getId());
		result.addObject("message", message);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		result.addObject("history", brotherhood.getHistory());
		return result;
	}

}
