
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	// Managed Repository
	@Autowired
	private BoxRepository	boxRepository;

	// Supporting Services
	@Autowired
	private ActorService	actorService;
	@Autowired
	private MessageService	messageService;


	// Constructor
	public BoxService() {
		super();
	}

	// CRUD

	public Box create() {
		final Box result = new Box();

		return result;
	}

	public Collection<Box> findAll() {
		Collection<Box> result;
		result = this.boxRepository.findAll();

		return result;
	}

	public void delete(final Box box) {
		Assert.isTrue(box.getCustom() == true);
		final Collection<Message> messages = this.messageService.findAll();
		for (final Message message : messages)
			if (message.getBoxes().contains(box)) {
				message.getBoxes().remove(box);
				this.messageService.simplesave(message);

			}
		final Actor actor = this.actorService.findByPrincipal();
		actor.getBoxes().remove(box);
		this.actorService.save(actor);
		this.actorService.flush();
		this.messageService.flush();
		this.boxRepository.delete(box);
	}

	public Box save(final Box box, final Actor actor) {
		final Box result;
		result = this.boxRepository.save(box);
		if (box.getId() == 0) {
			actor.getBoxes().add(box);
			this.flush();
			this.actorService.save(actor);
			this.actorService.flush();

		}
		return result;

	}
	public Box findOne(final int boxId) {
		Box result;

		result = this.boxRepository.findOne(boxId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.boxRepository.flush();
	}

	// Other Business Methods

	public Collection<Box> createDefaultBoxes() {
		final List<Box> result = new ArrayList<Box>();

		final Box inbox = new Box();
		inbox.setTitle("in box");
		inbox.setCustom(false);

		final Box outbox = new Box();
		outbox.setTitle("out box");
		outbox.setCustom(false);

		final Box trashbox = new Box();
		trashbox.setTitle("trash box");
		trashbox.setCustom(false);

		final Box spambox = new Box();
		spambox.setTitle("spam box");
		spambox.setCustom(false);

		final Box notificationBox = new Box();
		notificationBox.setTitle("notification box");
		notificationBox.setCustom(false);

		result.add(inbox);
		this.boxRepository.saveAndFlush(inbox);
		result.add(outbox);
		this.boxRepository.saveAndFlush(outbox);
		result.add(trashbox);
		this.boxRepository.saveAndFlush(trashbox);
		result.add(spambox);
		this.boxRepository.saveAndFlush(spambox);
		result.add(notificationBox);
		this.boxRepository.saveAndFlush(notificationBox);
		return result;
	}
}
