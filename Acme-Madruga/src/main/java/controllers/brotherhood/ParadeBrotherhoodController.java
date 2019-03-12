
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.BrotherhoodService;
import services.MemberService;
import services.MessageService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Float;
import domain.Message;
import domain.Parade;

@Controller
@RequestMapping("/parade/brotherhood")
public class ParadeBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private Validator				validator;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private MessageService			messageService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Parade> parades;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		parades = brotherhood.getParades();

		result = new ModelAndView("parade/list");
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");
		result.addObject("hasArea", hasArea);
		result.addObject("parades", parades);
		result.addObject("role", "brotherhood");
		result.addObject("requestURI", "parade/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int paradeId) {
		ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);

		try {
			this.paradeService.delete(parade);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}

		return result;
	}

	// Create ---------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Integer paradeId) {
		ModelAndView result;
		final Parade parade;

		parade = this.paradeService.create();
		result = this.createEditModelAndView(parade);

		result.addObject("parade", parade);
		result.addObject("requestURI", "parade/create.do");

		return result;

	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		ModelAndView result;
		final Parade parade;
		Brotherhood brotherhood;
		Collection<Parade> parades;

		brotherhood = this.brotherhoodService.findByPrincipal();
		parades = brotherhood.getParades();
		parade = this.paradeService.findOne(paradeId);
		Assert.isTrue(parades.contains(parade));
		final Collection<Float> floats = parade.getFloats();
		result = new ModelAndView("parade/display");
		result.addObject("floats", floats);
		result.addObject("parade", parade);
		result.addObject("role", "brotherhood");

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		Brotherhood brotherhood;
		Collection<Parade> parades;

		brotherhood = this.brotherhoodService.findByPrincipal();
		parades = brotherhood.getParades();
		parade = this.paradeService.findOne(paradeId);
		Assert.isTrue(parades.contains(parade));

		result = this.createEditModelAndView(parade);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("parade") @Valid final Parade parade, final BindingResult binding) {
		ModelAndView result;
		Parade parade2 = new Parade();
		parade2 = this.paradeService.reconstruct(parade, binding);
		this.validator.validate(parade2, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade2);
		else
			try {
				boolean isPublished = false;

				if (parade2.getId() == 0 && parade2.isDraftMode() == false)
					isPublished = true;
				if (parade2.getId() != 0) {
					final Parade previousParade = this.paradeService.findOne(parade2.getId());
					if (previousParade.isDraftMode() == true && parade2.isDraftMode() == false)
						isPublished = true;
				}

				if (isPublished) {
					final Message message = new Message();
					final Administrator admin = this.administratorService.findAll().iterator().next();
					final Collection<Actor> recipients = new ArrayList<Actor>();
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					recipients.addAll(this.memberService.findByBrotherhood(brotherhood.getId()));
					message.setMoment(new Date(System.currentTimeMillis() - 100));
					message.setSender(admin);
					message.setRecipients(recipients);
					message.setPriority("HIGH");
					message.setSpam(false);
					message.setTags("");
					message.setSubject("Enrolment accepted / Inscripcion aceptada");
					message.setBody("Brotherhood " + brotherhood.getTitle() + " has published parade " + parade2.getTitle() + " / " + "La hermandad " + brotherhood.getTitle() + " ha publicado la procesion" + parade2.getTitle());
					this.messageService.saveAdmin(message);
				}
				this.paradeService.save(parade2);

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
		final LinkedList<Float> floats = new LinkedList<Float>(this.brotherhoodService.findByPrincipal().getFloats());

		final LinkedList<Parade> parades = new LinkedList<Parade>(this.brotherhoodService.findByPrincipal().getParades());
		parades.remove(parade);
		for (final Parade p : parades)
			floats.removeAll(p.getFloats());
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);
		result.addObject("role", "brotherhood");
		result.addObject("hasArea", hasArea);
		result.addObject("message", message);
		result.addObject("floats", floats);
		result.addObject("requestURI", "parade/brotherhood/edit.do");
		return result;
	}
}
