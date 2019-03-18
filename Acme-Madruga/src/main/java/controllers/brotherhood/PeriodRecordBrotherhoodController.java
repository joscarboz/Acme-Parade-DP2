
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping("history/periodRecord")
public class PeriodRecordBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private PeriodRecordService	periodRecordService;


	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PeriodRecord res;

		res = this.periodRecordService.create();

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", res);
		result.addObject("id", res.getId());
		result.addObject("requestURI", "history/periodRecord/create.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		final ModelAndView result;
		PeriodRecord res;

		res = this.periodRecordService.findOne(periodRecordId);

		result = this.createEditModelAndView(res);
		result.addObject("requestURI", "history/periodRecord/edit.do");
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("periodRecord") @Valid final PeriodRecord res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.periodRecordService.save(res);
				result = this.list();

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "record.commit.error");
			}
		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int periodRecordId) {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		try {
			final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
			Assert.isTrue(this.brotherhoodService.findByPrincipal().getHistory().getPeriodRecords().contains(periodRecord));

			this.periodRecordService.delete(periodRecord);
			result = new ModelAndView("history/display");
			result.addObject("history", history);
			result.addObject("requestURI", "history/brotherhood/display.do");
			result.addObject("historyId", history.getId());

		} catch (final Throwable oops) {
			result = new ModelAndView("history/display");
			result.addObject("history", history);
			result.addObject("requestURI", "history/brotherhood/display.do");
			result.addObject("historyId", history.getId());

		}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PeriodRecord res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", res);
		result.addObject("id", res.getId());
		result.addObject("message", message);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		result.addObject("history", brotherhood.getHistory());
		return result;
	}

	public ModelAndView list() {
		final ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();

		result = new ModelAndView("history/display");
		result.addObject("history", history);
		result.addObject("requestURI", "history/brotherhood/display.do");
		result.addObject("historyId", history.getId());
		return result;
	}
}
