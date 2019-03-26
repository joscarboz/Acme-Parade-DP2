
package controllers.chapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ChapterService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Parade> parades;
		Collection<Brotherhood> brotherhoods;
		final List<Parade> paradesArea = new ArrayList<Parade>();
		Chapter chapter;

		chapter = this.chapterService.findByPrincipal();
		final Area area = chapter.getArea();
		if (area != null) {
			brotherhoods = this.brotherhoodService.findByArea(area.getId());

			for (final Brotherhood b : brotherhoods)
				paradesArea.addAll(b.getParades());
			for (final Parade p : paradesArea)
				if (p.isDraftMode() == true)
					paradesArea.remove(p);
			parades = paradesArea;
		} else
			parades = Collections.EMPTY_SET;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String date = sdf.format(new Date());

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("date", date);
		result.addObject("requestURI", "parade/chapter/list.do");

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int paradeId) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);

		try {
			parade.setStatus("accepted");
			this.paradeService.save(parade);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}

		return result;
	}

	// Rejection --------------------------------------------------------

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		result = this.createEditModelAndView(parade);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(final Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			Assert.isTrue(!parade.getRejectionReason().isEmpty(), "Reject reason cannot be null");
			parade.setStatus("rejected");
			this.paradeService.save(parade);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {

			result = this.createEditModelAndView(parade, "parade.commit.error");
		}
		return result;
	}

	// Ancillary methods
	// --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		return this.createEditModelAndView(parade, null);
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String message) {
		ModelAndView result;
		final List<Parade> paradesArea = new ArrayList<Parade>();
		Chapter chapter;
		final Collection<Parade> parades;
		Collection<Brotherhood> brotherhoods;

		chapter = this.chapterService.findByPrincipal();
		final Area area = chapter.getArea();
		brotherhoods = this.brotherhoodService.findByArea(area.getId());

		for (final Brotherhood b : brotherhoods)
			paradesArea.addAll(b.getParades());
		for (final Parade p : paradesArea)
			if (p.isDraftMode() == true)
				paradesArea.remove(p);
		parades = paradesArea;

		result = new ModelAndView("parade/reject");
		result.addObject("parade", parade);
		result.addObject("parades", parades);
		result.addObject("requestURI", "parade/chapter/reject.do");
		return result;
	}
}
