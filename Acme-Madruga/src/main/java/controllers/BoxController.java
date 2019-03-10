
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import services.MessageService;
import domain.Actor;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping("/box")
public class BoxController extends AbstractController {

	@Autowired
	private BoxService		boxService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private MessageService	messageService;


	public BoxController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		final ModelAndView result;
		final Box box;
		box = this.boxService.findOne(boxId);
		result = this.createEditModelAndView(box);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Box box, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(box);
		else
			try {
				Assert.isTrue(box.getCustom());
				final Actor actor = this.actorService.findByPrincipal();
				this.boxService.save(box, actor);
				final Collection<Message> messages;
				final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
				final Box cbox = this.boxService.create();
				box.setCustom(true);
				messages = this.messageService.findByBoxName("in box");

				result = new ModelAndView("message/list");
				result.addObject("messages", messages);
				result.addObject("boxes", boxes);
				result.addObject("requestURI", "message/list.do");
				result.addObject("box", cbox);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(box, "box.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int boxId) {
		ModelAndView result;
		final Box box = this.boxService.findOne(boxId);
		this.boxService.delete(box);
		final Collection<Message> messages;
		final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
		final Box cbox = this.boxService.create();
		box.setCustom(true);
		messages = this.messageService.findByBoxName("in box");

		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "message/list.do");
		result.addObject("box", cbox);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Box box) {
		ModelAndView result;

		result = this.createEditModelAndView(box, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final Box box, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("box/edit");
		result.addObject("box", box);
		result.addObject("message", messageCode);

		return result;
	}
}
