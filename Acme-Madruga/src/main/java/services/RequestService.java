
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.Authority;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed Repository
	@Autowired
	private RequestRepository		requestRepository;

	// Supporting Services

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AdministratorService	administratorService;


	public RequestService() {
		super();
	}
	// CRUD methods

	public Request create() {
		final Request result = new Request();

		result.setStatus("PENDING");
		result.setRow(0);
		result.setColumn(0);

		return result;
	}
	public Collection<Request> findAll() {
		Collection<Request> res;

		res = this.requestRepository.findAll();

		return res;
	}

	public Request findOne(final int resquestId) {
		Request res;

		res = this.requestRepository.findOne(resquestId);
		Assert.notNull(res);

		return res;
	}

	public Request save(final Request request) {

		Assert.notNull(request);
		final String status = request.getStatus();
		if (status.equals("APPROVED")) {
			Assert.isTrue(request.getColumn() != 0);
			Assert.isTrue(request.getColumn() != 0);

			final Collection<Request> all = request.getParade().getRequests();

			all.remove(request);

			for (final Request r : all) {
				final boolean rows = !(r.getRow() == request.getRow());
				final boolean columns = !(r.getColumn() == request.getColumn());
				Assert.isTrue(rows || columns);
			}
		}

		final Request res = this.requestRepository.save(request);

		if (request.getId() == 0) {
			final Collection<Request> requests = new HashSet<Request>(request.getParade().getRequests());
			requests.add(res);
			final Parade proc = request.getParade();
			proc.setRequests(requests);
			this.paradeService.save(proc);
			final Collection<Request> memberrequests = new HashSet<Request>(request.getMember().getRequests());
			memberrequests.add(res);
			final Member member = request.getMember();
			member.setRequests(memberrequests);
			this.memberService.save(member);

		}
		if (!request.getStatus().equals("PENDING")) {
			final Message message = new Message();
			final Administrator admin = this.administratorService.findAll().iterator().next();
			final Collection<Actor> recipients = new ArrayList<Actor>();
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			recipients.add(brotherhood);
			final Member member = request.getMember();
			recipients.add(member);
			message.setMoment(new Date(System.currentTimeMillis() - 100));
			message.setSender(admin);
			message.setRecipients(recipients);
			message.setPriority("HIGH");
			message.setSpam(false);
			message.setTags("");
			if (request.getStatus().equals("APPROVED")) {
				message.setSubject("Request APPROVED / Solicitud ACEPTADA");
				message.setBody("Brotherhood " + brotherhood.getTitle() + " has accepted " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + " request. Position given is row " + request.getRow() + ", column "
					+ request.getColumn() + ". / " + "La hermandad  " + brotherhood.getTitle() + " ha aceptado la solicitud de " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname());
			}

			if (request.getStatus().equals("REJECTED")) {
				message.setSubject("Request REJECTED / Solicitud RECHAZADA");
				message.setBody("Brotherhood " + brotherhood.getTitle() + " has rejected " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + " request. The rejection reason is the following: " + request.getRejectionReason()
					+ " / " + "La hermandad  " + brotherhood.getTitle() + " ha rechazado la solicitud de " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + ". La razón de rechazo es la siguiente: "
					+ request.getRejectionReason());
			}
			this.messageService.saveAdmin(message);
		}

		return res;
	}

	public void delete(final Request request) {

		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(this.requestRepository.exists(request.getId()));

		final Actor principal = this.actorService.findByPrincipal();
		final Authority memberauth = new Authority();
		memberauth.setAuthority(Authority.MEMBER);

		if (principal.getUserAccount().getAuthorities().contains(memberauth))
			Assert.isTrue(request.getMember().getId() == principal.getId());

		Collection<Request> requests = request.getParade().getRequests();
		requests.remove(request);
		final Parade p = request.getParade();
		p.setRequests(requests);
		this.paradeService.update(p);

		requests = request.getMember().getRequests();
		requests.remove(request);
		final Member member = request.getMember();
		member.setRequests(requests);
		this.actorService.save(member);

		this.requestRepository.delete(request);

	}

	public void flush() {
		this.requestRepository.flush();
	}

	// Other Business Methods

	public Collection<Request> findByParadeAndStatus(final int paradeId, final String status) {
		Assert.isTrue(paradeId != 0);
		Assert.isTrue(status == "PENDING" || status == "APPROVED" || status == "REJECTED");

		final Collection<Request> res = this.requestRepository.findByParadeAndStatus(paradeId, status);
		Assert.notNull(res);

		return res;

	}

	public Request suggestRowAndColumn(final Request request) {

		Assert.notNull(request);

		final Request res = request;
		final Collection<Request> all = this.findAll();
		int suggestedRow = 1;
		int suggestedColumn = 1;

		for (final Request r : all)

			if (r.getRow() == suggestedRow && r.getColumn() == suggestedColumn) {
				suggestedColumn++;
				if (suggestedColumn > 2) {
					suggestedColumn = 1;
					suggestedRow++;
				}
			}

		res.setRow(suggestedRow);
		res.setColumn(suggestedColumn);

		return res;
	}

	public Request acceptRequest(final Request request) {
		final String status = request.getStatus();
		Assert.isTrue(status.equals("PENDING"));

		final Actor actor = this.actorService.findByPrincipal();
		final Brotherhood brotherhood = this.brotherhoodService.findByRequest(request);
		Assert.isTrue(actor.getId() == brotherhood.getId());

		request.setStatus("APPROVED");
		final Request suggested = this.suggestRowAndColumn(request);

		return suggested;
	}

	public Request rejectRequest(final Request request) {
		final String status = request.getStatus();
		Assert.isTrue(status.equals("PENDING"));

		final Actor actor = this.actorService.findByPrincipal();
		final Brotherhood brotherhood = this.brotherhoodService.findByRequest(request);
		Assert.isTrue(actor.getId() == brotherhood.getId());

		request.setStatus("REJECTED");
		request.setColumn(0);
		request.setRow(0);

		return request;
	}

	public Collection<Request> findByMember(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(member.getId() != 0);
		Collection<Request> result;

		result = this.requestRepository.findByMemberId(member.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findByBrotherhood(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Assert.isTrue(brotherhood.getId() != 0);
		Collection<Request> result;

		result = this.requestRepository.findByBrotherhoodId(brotherhood.getId());
		Assert.notNull(result);

		return result;
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Request reconstruct(final Request request, final BindingResult binding) {
		Request result;

		if (request.getId() == 0)
			result = request;
		else {
			result = this.requestRepository.findOne(request.getId());

			result.setColumn(request.getColumn());
			result.setRow(request.getRow());
			result.setRejectionReason(request.getRejectionReason());
			result.setStatus(request.getStatus());

			this.validator.validate(result, binding);
		}

		return result;
	}

	// Methods for tests

	public Request registerPrincipal(final Integer paradeId) {
		final Parade parade = this.paradeService.findOne(paradeId);
		final Member member = this.memberService.findByPrincipal();

		final Request request = this.create();
		request.setMember(member);
		request.setParade(parade);

		return request;

	}

}
