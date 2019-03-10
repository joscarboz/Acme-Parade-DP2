
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import forms.BoxMoveForm;
import forms.MessageForm;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private BoxService		boxService;


	public MessageController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String boxName) {
		final ModelAndView result;
		final Collection<Message> messages;
		final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
		final Box box = this.boxService.create();
		box.setCustom(true);
		messages = this.messageService.findByBoxName(boxName);
		
		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("boxes", boxes);
		result.addObject("boxName", boxName);
		result.addObject("requestURI", "message/list.do");
		result.addObject("box", box);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId) {
		final ModelAndView result;
		final Message message;
		message = this.messageService.findOne(messageId);
		final List<Actor> actors = new ArrayList<Actor>();
		actors.add(message.getSender());
		actors.addAll(message.getRecipients());
		Assert.isTrue(actors.contains(this.actorService.findByPrincipal()));
		result = new ModelAndView("message/display");
		result.addObject("displayedmessage", message);
		result.addObject("requestURI", "message/display.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() throws Exception {
		ModelAndView result;
		MessageForm messageForm;

		messageForm = new MessageForm();
		result = this.createEditModelAndView(messageForm);

		return result;
	}

	@RequestMapping(value = "/createbox", method = RequestMethod.POST, params = "savebox")
	public ModelAndView createbox(@Valid final Box box, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = new ModelAndView("redirect:list.do?boxName=in box");
		else
			try {

				final Actor actor = this.actorService.findByPrincipal();
				final Collection<String> boxTitles = new ArrayList<String>();
				for (final Box actorbox : actor.getBoxes())
					boxTitles.add(actorbox.getTitle());
				Assert.isTrue(!boxTitles.contains(box.getTitle()));
				this.boxService.save(box, actor);
				result = new ModelAndView("redirect:list.do?boxName=in box");

			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:list.do?boxName=in box");

			}
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView send(@Valid final MessageForm messageForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageForm);
		else
			try {
				final Message message = this.messageService.create();
				message.setSender(this.actorService.findByPrincipal());
				message.setBody(messageForm.getBody());
				message.setPriority(messageForm.getPriority());
				message.setTags(messageForm.getTags());
				message.setSubject(messageForm.getSubject());
				final String recipientString = messageForm.getRecipient();
				final Actor recipient = this.actorService.findByUsername(recipientString);
				Assert.isTrue(recipient.getUserAccount().getUsername().compareTo(this.actorService.findByPrincipal().getUserAccount().getUsername()) != 0.);
				final List<Actor> recipients = new ArrayList<Actor>(message.getRecipients());
				recipients.add(recipient);
				message.setRecipients(recipients);
				this.messageService.save(message);
				result = new ModelAndView("redirect:list.do?boxName=in box");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageForm, "message.commit.error");
				oops.printStackTrace();
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId) {
		final Actor actor = this.actorService.findByPrincipal();
		final Message message = this.messageService.findOne(messageId);
		final List<Box> actorBoxes = new ArrayList<Box>(actor.getBoxes());
		final List<Box> messageBoxes = new ArrayList<Box>(message.getBoxes());
		actorBoxes.retainAll(messageBoxes);
		final Box box = actorBoxes.get(0);
		if (box.getTitle().equals("trash box")) {
			message.getBoxes().remove(box);
			if (message.getBoxes().isEmpty())
				this.messageService.delete(message);
		} else {
			actorBoxes.clear();
			Box trashbox = new Box();
			actorBoxes.addAll(actor.getBoxes());
			for (final Box box2 : actorBoxes)
				if (box2.getTitle().equals("trash box"))
					trashbox = box2;
			message.getBoxes().remove(box);
			message.getBoxes().add(trashbox);			
			this.messageService.simplesave(message);

		}
		final Collection<Message> messages;
		final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
		final Box cbox = this.boxService.create();
		cbox.setCustom(true);
		messages = this.messageService.findByBoxName("trash box");
		ModelAndView result;
		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "message/list.do");
		result.addObject("boxName", "trash box");
		result.addObject("box", cbox);
		return result;
	}
	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public ModelAndView createBroadcast() throws Exception {
		ModelAndView result;
		MessageForm messageForm;

		messageForm = new MessageForm();
		result = this.createEditModelAndView(messageForm);
		result.setViewName("message/broadcast");

		return result;
	}

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "broadcast")
	public ModelAndView broadcast(@Valid final MessageForm messageForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(messageForm);
			result.setViewName("message/broadcast");
		} else

			try {
				final Message message = this.messageService.create();
				message.setSender(this.actorService.findByPrincipal());
				message.setBody(messageForm.getBody());
				message.setPriority(messageForm.getPriority());
				message.setTags(messageForm.getTags());
				message.setSubject(messageForm.getSubject());
				final Collection<Actor> recipients = this.actorService.findAll();
				message.setRecipients(recipients);
				this.messageService.broadcast(message);
				result = new ModelAndView("redirect:list.do?boxName=notification box");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageForm, "message.commit.error");
				result.setViewName("message/broadcast");
				oops.printStackTrace();
			}

		return result;
	}

	@RequestMapping(value = "/boxmove", method = RequestMethod.GET)
	public ModelAndView boxmove(@RequestParam final int messageId) {
		final ModelAndView result = new ModelAndView("message/boxmove");
		final BoxMoveForm boxMoveForm = new BoxMoveForm();
		boxMoveForm.setMessageId(messageId);
		boxMoveForm.setTitle("");
		final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
		result.addObject("boxMoveForm", boxMoveForm);
		result.addObject("boxes", boxes);

		return result;
	}

	@RequestMapping(value = "/boxmove", method = RequestMethod.POST, params = "save")
	public ModelAndView boxmovesave(@Valid final BoxMoveForm boxMoveForm, final BindingResult binding) {
		final Message message = this.messageService.findOne(boxMoveForm.getMessageId());
		final Actor actor = this.actorService.findByPrincipal();
		final List<Box> actorBoxes = new ArrayList<Box>(actor.getBoxes());
		final List<Box> messageBoxes = new ArrayList<Box>(message.getBoxes());
		actorBoxes.retainAll(messageBoxes);
		final Box currentBox = actorBoxes.get(0);
		Box newBox = null;
		actorBoxes.clear();
		actorBoxes.addAll(actor.getBoxes());
		for (final Box box : actorBoxes)
			if (box.getTitle().equals(boxMoveForm.getTitle())) {
				newBox = box;
				break;
			}
		Assert.notNull(newBox);
		message.getBoxes().remove(currentBox);
		message.getBoxes().add(newBox);
		this.messageService.simplesave(message);

		final Collection<Message> messages;
		final Collection<Box> boxes = this.actorService.findByPrincipal().getBoxes();
		final Box cbox = this.boxService.create();
		cbox.setCustom(true);
		messages = this.messageService.findByBoxName(boxMoveForm.getTitle());
		ModelAndView result;
		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("boxName", boxMoveForm.getTitle());
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "message/list.do");
		result.addObject("box", cbox);
		return result;

	}
	protected ModelAndView createEditModelAndView(final MessageForm messageForm) {
		ModelAndView result;

		result = this.createEditModelAndView(messageForm, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final MessageForm messageForm, final String messageCode) {
		ModelAndView result;

		final String requestURI = "message/create.do";

		result = new ModelAndView("message/edit");
		result.addObject("messageForm", messageForm);
		result.addObject("message", messageCode);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
