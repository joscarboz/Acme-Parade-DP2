
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ProcessionService;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Constructor

	public ProcessionController() {
		super();
	}

	// Listing Processions from a Brotherhood

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int brotherhoodId) {
		ModelAndView result;
		Collection<Procession> processions;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		processions = brotherhood.getProcessions();
		final Collection<Procession> res = new ArrayList<>();

		for (final Procession p : processions)
			if (p.isDraftMode() == false)
				res.add(p);

		result = new ModelAndView("procession/list");
		result.addObject("processions", res);
		result.addObject("requestURI", "processions/list.do");

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		ModelAndView result;
		final Procession procession;

		procession = this.processionService.findOne(processionId);
		final Collection<Float> floats = procession.getFloats();
		result = new ModelAndView("procession/display");
		result.addObject("floats", floats);
		result.addObject("procession", procession);
		result.addObject("role", "none");

		return result;
	}
}
