
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
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("history/linkRecord")
public class LinkRecordBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private LinkRecordService	linkRecordService;


	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LinkRecord res;

		res = this.linkRecordService.create();

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", res);
		result.addObject("id", res.getId());
		result.addObject("brotherhoods", this.brotherhoodService.findAll());
		result.addObject("requestURI", "history/linkRecord/create.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		final ModelAndView result;
		LinkRecord res;

		res = this.linkRecordService.findOne(linkRecordId);

		result = this.createEditModelAndView(res);
		result.addObject("requestURI", "history/linkRecord/edit.do");
		result.addObject("brotherhoods", this.brotherhoodService.findAll());
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("linkRecord") @Valid final LinkRecord res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.linkRecordService.save(res);
				result = this.list();

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "record.commit.error");
			}
		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int linkRecordId) {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		try {
			final LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
			Assert.isTrue(this.brotherhoodService.findByPrincipal().getHistory().getLinkRecords().contains(linkRecord));

			this.linkRecordService.delete(linkRecord);
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

	protected ModelAndView createEditModelAndView(final LinkRecord res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final LinkRecord res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", res);
		result.addObject("id", res.getId());
		result.addObject("message", message);
		result.addObject("brotherhoods", this.brotherhoodService.findAll());
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
