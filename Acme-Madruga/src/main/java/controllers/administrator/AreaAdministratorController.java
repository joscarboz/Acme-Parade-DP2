
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import controllers.AbstractController;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/area/administrator")
public class AreaAdministratorController extends AbstractController {

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Area> areas;

		areas = this.areaService.findAll();

		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		final boolean b = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

		return result;
	}

	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Area area;

		area = this.areaService.create();

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("notUsed", false);
		result.addObject("requestURI", "area/administrator/edit.do");
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;
		Area area;

		area = this.areaService.findOne(areaId);

		result = this.createEditModelAndView(area);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("area") @Valid final Area area, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(area);
		else
			try {
				this.areaService.save(area);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(area, "area.commit.error");
			}
		return result;
	}

	// Delete --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Area area, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.areaService.delete(area);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(area, "area.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Area area) {
		return this.createEditModelAndView(area, null);
	}

	protected ModelAndView createEditModelAndView(final Area area, final String message) {
		final ModelAndView result;

		boolean notUsed;

		if (area.getId() == 0)
			notUsed = false;
		else {
			final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findByArea(area.getId());
			notUsed = brotherhoods.isEmpty();
		}

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("notUsed", notUsed);
		result.addObject("message", message);
		result.addObject("requestURI", "area/administrator/edit.do");
		return result;
	}

}
