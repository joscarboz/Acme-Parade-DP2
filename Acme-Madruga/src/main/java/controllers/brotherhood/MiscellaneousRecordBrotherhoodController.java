
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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("history/miscellaneousRecord")
public class MiscellaneousRecordBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService			brotherhoodService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MiscellaneousRecord res;

		res = this.miscellaneousRecordService.create();

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", res);
		result.addObject("id", res.getId());
		result.addObject("requestURI", "history/miscellaneousRecord/create.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		final ModelAndView result;
		MiscellaneousRecord res;

		res = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		result = this.createEditModelAndView(res);
		result.addObject("requestURI", "history/miscellaneousRecord/edit.do");
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("miscellaneousRecord") @Valid final MiscellaneousRecord res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.miscellaneousRecordService.save(res);
				result = this.list();

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "record.commit.error");
			}
		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		try {
			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			Assert.isTrue(this.brotherhoodService.findByPrincipal().getHistory().getMiscellaneousRecords().contains(miscellaneousRecord));

			this.miscellaneousRecordService.delete(miscellaneousRecord);
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

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", res);
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
