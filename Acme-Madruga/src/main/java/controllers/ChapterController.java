
package controllers;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ChapterService;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.SocialProfile;

@Controller
@RequestMapping("/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ChapterService		chapterService;


	//Constructor

	public ChapterController() {
		super();
	}

	//Listing for all users

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Chapter> chapters;

		chapters = this.chapterService.findAll();

		result = new ModelAndView("chapter/list");
		result.addObject("chapters", chapters);
		result.addObject("requestURI", "chapter/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int chapterId) {
		ModelAndView result;
		final Chapter chapter;
		final Collection<Brotherhood> brotherhoods;
		Area area;

		chapter = this.chapterService.findOne(chapterId);
		area = chapter.getArea();
		if (area != null)
			brotherhoods = this.brotherhoodService.findByArea(area.getId());
		else
			brotherhoods = Collections.EMPTY_SET;

		result = new ModelAndView("chapter/display");
		final Collection<SocialProfile> socialProfiles = chapter.getSocialProfiles();

		result.addObject("socialProfiles", socialProfiles);

		result.addObject("brotherhoods", brotherhoods);

		result.addObject("chapter", chapter);
		return result;
	}
}
