
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ProclaimService;
import domain.Chapter;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim")
public class ProclaimController extends AbstractController {

	@Autowired
	private ProclaimService	proclaimService;

	@Autowired
	private ChapterService	chapterService;


	//Constructor

	public ProclaimController() {
		super();
	}

	// List --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int chapterId) {
		ModelAndView result;
		Chapter chapter;
		Collection<Proclaim> proclaims;

		chapter = this.chapterService.findOne(chapterId);
		proclaims = chapter.getProclaims();

		result = new ModelAndView("proclaim/list");
		result.addObject("proclaims", proclaims);
		result.addObject("requestURI", "proclaim/list.do");
		result.addObject("role", "");

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
		result.addObject("cancelURL", "proclaim/list.do");

		return result;
	}

}
