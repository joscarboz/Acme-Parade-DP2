
package controllers.administrator;

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

import services.MemberService;
import services.PositionService;
import controllers.AbstractController;
import domain.Member;
import domain.Position;

@Controller
@RequestMapping("/position/administrator")
public class PositionAdministratorController extends AbstractController {

	@Autowired
	private PositionService	positionService;

	@Autowired
	private MemberService	memberService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAll();

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);

		return result;
	}

	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Position position;

		position = this.positionService.create();

		result = new ModelAndView("position/edit");
		result.addObject("position", position);
		result.addObject("notUsed", false);
		result.addObject("requestURI", "position/administrator/edit.do");
		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findOne(positionId);
		result = new ModelAndView("position/display");
		result.addObject("position", position);

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findOne(positionId);

		result = this.createEditModelAndView(position);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("position") @Valid final Position position, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(position);
		else
			try {
				//No sé si esto es necesario, no se especifica pero sería lo logico que no puedan tener el mismo título dos posiciones diferentes
				/*
				 * final Collection<Position> allpositions = this.positionService.findAll();
				 * final List<String> titles = new ArrayList<>();
				 * final List<String> spanishTitles = new ArrayList<>();
				 * for (final Position p : allpositions) {
				 * titles.add(p.getTitle().toLowerCase());
				 * spanishTitles.add(p.getSpanishTitle().toLowerCase());
				 * }
				 * final String positionTitle = position.getTitle();
				 * final String positionSpanishTitle = position.getSpanishTitle();
				 * final boolean contains = !titles.contains(positionTitle);
				 * final boolean contains2 = !titles.contains(positionSpanishTitle);
				 * Assert.isTrue(contains && contains2);
				 */
				this.positionService.save(position);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}
		return result;
	}

	// Delete --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Position position, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.positionService.delete(position);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(position, "position.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Position position) {
		return this.createEditModelAndView(position, null);
	}

	protected ModelAndView createEditModelAndView(final Position position, final String message) {
		final ModelAndView result;

		boolean notUsed;

		if (position.getId() == 0)
			notUsed = false;
		else {
			final Collection<Member> members = this.memberService.findByPosition(position.getId());
			notUsed = members.isEmpty();
		}

		result = new ModelAndView("position/edit");
		result.addObject("position", position);
		result.addObject("notUsed", notUsed);
		result.addObject("message", message);
		result.addObject("requestURI", "position/administrator/edit.do");
		return result;
	}

}
