
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
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("history/legalRecord")
public class LegalRecordBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private LegalRecordService	legalRecordService;


	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalRecord res;

		res = this.legalRecordService.create();

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", res);
		result.addObject("id", res.getId());
		result.addObject("requestURI", "history/legalRecord/create.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		final ModelAndView result;
		LegalRecord res;

		res = this.legalRecordService.findOne(legalRecordId);

		result = this.createEditModelAndView(res);
		result.addObject("requestURI", "history/legalRecord/edit.do");
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("legalRecord") @Valid final LegalRecord res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.legalRecordService.save(res);
				result = this.list();

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "record.commit.error");
			}
		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int legalRecordId) {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		try {
			final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
			Assert.isTrue(this.brotherhoodService.findByPrincipal().getHistory().getLegalRecords().contains(legalRecord));

			this.legalRecordService.delete(legalRecord);
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

	protected ModelAndView createEditModelAndView(final LegalRecord res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final LegalRecord res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", res);
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
