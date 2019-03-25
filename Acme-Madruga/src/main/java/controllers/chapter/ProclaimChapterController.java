
package controllers.chapter;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ProclaimService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim/chapter")
public class ProclaimChapterController extends AbstractController {

	@Autowired
	private ProclaimService	proclaimService;

	@Autowired
	private ChapterService	chapterService;


	//Constructor

	public ProclaimChapterController() {
		super();
	}

	// List --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Chapter chapter;
		Collection<Proclaim> proclaims;

		chapter = this.chapterService.findByPrincipal();
		proclaims = chapter.getProclaims();

		result = new ModelAndView("proclaim/list");
		result.addObject("proclaims", proclaims);
		result.addObject("requestURI", "proclaim/chapter/list.do");
		result.addObject("role", "chapter/");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int proclaimId) {
		final ModelAndView result;
		Proclaim proclaim;

		proclaim = this.proclaimService.findOne(proclaimId);
		result = new ModelAndView("proclaim/display");
		result.addObject("proclaim", proclaim);
		result.addObject("cancelURL", "proclaim/chapter/list.do");

		return result;
	}

	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Proclaim proclaim;

		proclaim = this.proclaimService.create();

		result = new ModelAndView("proclaim/edit");
		result.addObject("proclaim", proclaim);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("proclaim") @Valid final Proclaim proclaim, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(proclaim);
		else
			try {
				this.proclaimService.save(proclaim);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(proclaim, "area.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Proclaim proclaim) {
		return this.createEditModelAndView(proclaim, null);
	}

	protected ModelAndView createEditModelAndView(final Proclaim proclaim, final String message) {
		final ModelAndView result;

		result = new ModelAndView("proclaim/edit");
		result.addObject("proclaim", proclaim);
		result.addObject("message", message);
		return result;
	}

}
