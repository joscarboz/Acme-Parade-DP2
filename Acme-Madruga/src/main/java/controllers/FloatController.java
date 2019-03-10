
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FloatService;
import domain.Brotherhood;
import domain.Float;

@Controller
@RequestMapping("/float")
public class FloatController extends AbstractController {

	@Autowired
	private FloatService		floatService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Constructor

	public FloatController() {
		super();
	}

	//Listing for all users

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId) {
		ModelAndView result;
		Brotherhood brotherhood;
		Collection<Float> floats;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		floats = brotherhood.getFloats();

		result = new ModelAndView("float/list");
		result.addObject("floats", floats);
		result.addObject("requestURI", "float/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		final ModelAndView result;
		Float res;

		res = this.floatService.findOne(floatId);
		result = new ModelAndView("float/display");
		result.addObject("res", res);
		result.addObject("role", "none");

		return result;
	}
}
