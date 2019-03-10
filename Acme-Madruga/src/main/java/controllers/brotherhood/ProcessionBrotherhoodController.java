
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
import services.ProcessionService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Float;
import domain.Message;
import domain.Procession;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ProcessionService		processionService;

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
		final Collection<Procession> processions;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		processions = brotherhood.getProcessions();

		result = new ModelAndView("procession/list");
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");
		result.addObject("hasArea", hasArea);
		result.addObject("processions", processions);
		result.addObject("role", "brotherhood");
		result.addObject("requestURI", "procession/brotherhood/list.do");

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int processionId) {
		ModelAndView result;
		final Procession procession = this.processionService.findOne(processionId);

		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}

		return result;
	}

	// Create ---------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Integer processionId) {
		ModelAndView result;
		final Procession procession;

		procession = this.processionService.create();
		result = this.createEditModelAndView(procession);

		result.addObject("procession", procession);
		result.addObject("requestURI", "procession/create.do");

		return result;

	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		ModelAndView result;
		final Procession procession;
		Brotherhood brotherhood;
		Collection<Procession> processions;

		brotherhood = this.brotherhoodService.findByPrincipal();
		processions = brotherhood.getProcessions();
		procession = this.processionService.findOne(processionId);
		Assert.isTrue(processions.contains(procession));
		final Collection<Float> floats = procession.getFloats();
		result = new ModelAndView("procession/display");
		result.addObject("floats", floats);
		result.addObject("procession", procession);
		result.addObject("role", "brotherhood");

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;
		Brotherhood brotherhood;
		Collection<Procession> processions;

		brotherhood = this.brotherhoodService.findByPrincipal();
		processions = brotherhood.getProcessions();
		procession = this.processionService.findOne(processionId);
		Assert.isTrue(processions.contains(procession));

		result = this.createEditModelAndView(procession);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("procession") @Valid final Procession procession, final BindingResult binding) {
		ModelAndView result;
		Procession procession2 = new Procession();
		procession2 = this.processionService.reconstruct(procession, binding);
		this.validator.validate(procession2, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession2);
		else
			try {
				boolean isPublished = false;

				if (procession2.getId() == 0 && procession2.isDraftMode() == false)
					isPublished = true;
				if (procession2.getId() != 0) {
					final Procession previousProcession = this.processionService.findOne(procession2.getId());
					if (previousProcession.isDraftMode() == true && procession2.isDraftMode() == false)
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
					message.setBody("Brotherhood " + brotherhood.getTitle() + " has published procession " + procession2.getTitle() + " / " + "La hermandad " + brotherhood.getTitle() + " ha publicado la procesion" + procession2.getTitle());
					this.messageService.saveAdmin(message);
				}
				this.processionService.save(procession2);

				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {

				result = this.createEditModelAndView(procession, "procession.commit.error");
			}
		return result;
	}

	// Ancillary methods
	// --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Procession procession) {
		return this.createEditModelAndView(procession, null);
	}

	protected ModelAndView createEditModelAndView(final Procession procession, final String message) {
		ModelAndView result;
		final LinkedList<Float> floats = new LinkedList<Float>(this.brotherhoodService.findByPrincipal().getFloats());

		final LinkedList<Procession> processions = new LinkedList<Procession>(this.brotherhoodService.findByPrincipal().getProcessions());
		processions.remove(procession);
		for (final Procession p : processions)
			floats.removeAll(p.getFloats());
		final Boolean hasArea = !this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea");

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession);
		result.addObject("role", "brotherhood");
		result.addObject("hasArea", hasArea);
		result.addObject("message", message);
		result.addObject("floats", floats);
		result.addObject("requestURI", "procession/brotherhood/edit.do");
		return result;
	}
}
