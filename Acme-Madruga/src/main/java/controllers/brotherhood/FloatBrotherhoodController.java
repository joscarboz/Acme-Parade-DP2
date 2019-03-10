
package controllers.brotherhood;

import java.util.Collection;

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
import services.FloatService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;

@Controller
@RequestMapping("/float/brotherhood")
public class FloatBrotherhoodController extends AbstractController {

	@Autowired
	private FloatService		floatService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Float> floats;

		final Brotherhood principal = this.brotherhoodService.findByPrincipal();

		floats = principal.getFloats();

		result = new ModelAndView("float/list");
		result.addObject("floats", floats);

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		final ModelAndView result;
		Float res;
		Collection<Float> floats;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		floats = brotherhood.getFloats();
		res = this.floatService.findOne(floatId);
		Assert.isTrue(floats.contains(res));
		result = new ModelAndView("float/display");
		result.addObject("res", res);
		result.addObject("role", "brotherhood");

		return result;
	}

	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Float res;

		res = this.floatService.create();

		result = new ModelAndView("float/edit");
		result.addObject("float", res);
		result.addObject("id", res.getId());
		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		Float res;

		res = this.floatService.findOne(floatId);

		result = this.createEditModelAndView(res);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("float") @Valid final Float res, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(res);
		else
			try {
				this.floatService.save(res);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(res, "float.commit.error");
			}
		return result;
	}

	// Delete --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Float res, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.floatService.delete(res);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(res, "float.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Float res) {
		return this.createEditModelAndView(res, null);
	}

	protected ModelAndView createEditModelAndView(final Float res, final String message) {
		final ModelAndView result;

		result = new ModelAndView("float/edit");
		result.addObject("float", res);
		result.addObject("id", res.getId());
		result.addObject("message", message);
		result.addObject("requestURI", "position/administrator/edit.do");
		return result;
	}

}
