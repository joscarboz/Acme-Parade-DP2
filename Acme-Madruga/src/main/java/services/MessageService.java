
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.Authority;
import security.LoginService;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed Repository
	@Autowired
	private MessageRepository		messageRepository;

	// Supporting Services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructor
	public MessageService() {
		super();
	}

	// CRUD

	public Message create() {
		final Message result = new Message();

		result.setMoment(new Date(System.currentTimeMillis() - 100));
		result.setBoxes(Collections.<Box> emptySet());
		result.setRecipients(Collections.<Actor> emptySet());
		result.setSpam(false);
		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();

		return result;
	}

	public Collection<Message> findBySender(final Actor actor) {
		return this.messageRepository.findbySender(actor.getId());
	}

	public void delete(final Message message) {
		this.messageRepository.delete(message);

	}

	public Message simplesave(final Message message) {
		final Message result = this.messageRepository.save(message);
		return result;
	}

	public Message save(final Message message) {
		Message result;
		Assert.isTrue(message.getId() == 0);
		Assert.isTrue(message.getSender().getUserAccount().getId() == LoginService.getPrincipal().getId());
		Assert.isTrue(message.getPriority().equals("HIGH") || message.getPriority().equals("LOW") || message.getPriority().equals("MEDIUM"));
		final boolean a = this.actorService.isSpam(message.getBody());
		final boolean b = this.actorService.isSpam(message.getSubject());
		final boolean c = this.actorService.isSpam(message.getTags());

		final Collection<Box> messageBoxes = new ArrayList<Box>();
		final Collection<Box> senderBoxes = message.getSender().getBoxes();
		for (final Box box : senderBoxes)
			if (box.getTitle().equals("out box")) {
				messageBoxes.add(box);
				break;
			}

		String outbox = "in box";

		if (a || b || c) {
			message.setSpam(true);
			outbox = "spam box";
		}

		final Collection<Actor> recipients = message.getRecipients();
		for (final Actor actor : recipients) {
			final Collection<Box> recipientBox = actor.getBoxes();
			for (final Box box : recipientBox)
				if (box.getTitle().equals(outbox)) {
					messageBoxes.add(box);
					break;
				}
		}

		message.setBoxes(messageBoxes);

		result = this.messageRepository.save(message);
		return result;
	}

	public Message findOne(final int boxId) {
		Message result;

		result = this.messageRepository.findOne(boxId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.messageRepository.flush();
	}

	// Other Business Methods

	public Message updateMessageBoxes(final Message message) {
		Message result;
		Assert.isTrue(message.getSender().getUserAccount().getId() == LoginService.getPrincipal().getId());
		boolean check = true;
		final Collection<Box> actorBoxes = this.actorService.findByPrincipal().getBoxes();
		for (final Box b : message.getSender().getBoxes())
			if (!actorBoxes.contains(b)) {
				check = false;
				break;
			}
		Assert.isTrue(check);
		result = this.messageRepository.save(message);
		return result;

	}

	public Collection<Message> findByBoxName(final String boxName) {
		final Actor actor = this.actorService.findByPrincipal();
		final Collection<Actor> actors = this.actorService.findAll();
		final Collection<Box> boxes = actor.getBoxes();
		final List<Message> messages = new ArrayList<Message>(this.findAll());
		final Collection<Message> result = new ArrayList<Message>();

		for (final Box box : boxes)
			if (box.getTitle().equals(boxName)) {
				for (final Message m : messages)
					if (m.getBoxes().contains(box))
						result.add(m);
				break;
			}
		return result;
	}

	public Message saveAdmin(final Message message) {
		Message result;
		Assert.isTrue(message.getId() == 0);

		final Collection<Box> messageBoxes = new ArrayList<Box>();
		/*
		 * final Collection<Box> senderBoxes = message.getSender().getBoxes();
		 * for (final Box box : senderBoxes) if
		 * (box.getTitle().equals("out box")) { messageBoxes.add(box); break; }
		 */
		final String outbox = "notification box";

		final Collection<Actor> recipients = message.getRecipients();
		for (final Actor actor : recipients) {
			final Collection<Box> recipientBox = actor.getBoxes();
			for (final Box box : recipientBox)
				if (box.getTitle().equals(outbox)) {
					messageBoxes.add(box);
					break;
				}
		}

		message.setBoxes(messageBoxes);
		this.administratorService.save(this.administratorService.findOne(message.getSender().getId()));
		result = this.messageRepository.save(message);
		return result;
	}

	public Message broadcast(final Message message) {
		Message result;
		Assert.isTrue(message.getId() == 0);
		Assert.isTrue(message.getSender().getUserAccount().getId() == LoginService.getPrincipal().getId());
		final Authority a = new Authority();
		a.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(a));
		Assert.isTrue(message.getPriority().equals("HIGH") || message.getPriority().equals("LOW") || message.getPriority().equals("MEDIUM"));

		final Collection<Box> messageBoxes = new ArrayList<Box>();
		final Collection<Box> senderBoxes = this.actorService.findByPrincipal().getBoxes();
		for (final Box box : senderBoxes)
			if (box.getTitle().equals("out box")) {
				messageBoxes.add(box);
				break;
			}

		final String outbox = "notification box";

		final Collection<Actor> recipients = this.actorService.findAll();
		recipients.remove(this.actorService.findByPrincipal());
		for (final Actor actor : recipients) {
			final Collection<Box> recipientBox = actor.getBoxes();
			for (final Box box : recipientBox)
				if (box.getTitle().equals(outbox)) {
					messageBoxes.add(box);
					break;
				}
		}

		message.setBoxes(messageBoxes);

		result = this.messageRepository.save(message);
		return result;
	}
}
