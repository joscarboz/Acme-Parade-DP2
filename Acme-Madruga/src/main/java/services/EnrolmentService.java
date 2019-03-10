
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.EnrolmentRepository;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Message;

@Service
@Transactional
public class EnrolmentService {

	@Autowired
	private EnrolmentRepository		enrolmentRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MessageService			messageService;


	public EnrolmentService() {
		super();
	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAll();

		return result;
	}

	public Enrolment findOne(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);

		return result;
	}

	public Enrolment create(final Brotherhood brotherhood) {

		Assert.isTrue(!brotherhood.getArea().getName().equals("defaultArea"));
		final Enrolment result = new Enrolment();

		final Member member = this.memberService.findByPrincipal();
		result.setMember(member);
		result.setMoment(Calendar.getInstance().getTime());
		result.setBrotherhood(brotherhood);
		return result;
	}

	public Enrolment save(final Enrolment enrolment) {

		Assert.notNull(enrolment);

		// En caso de no ser un nuevo enrolment sino una baja que vuelve se ha
		// de setear el moment a null y asi detectarlo en este if para volver a
		// setear de nuevo el moment
		if (enrolment.getId() == 0 || enrolment.getMoment() == null)
			enrolment.setMoment(new Date(System.currentTimeMillis() - 1));

		final Enrolment result = this.enrolmentRepository.save(enrolment);

		final Brotherhood brotherhood = result.getBrotherhood();

		brotherhood.getEnrolments().add(result);
		final Message message = new Message();
		final Administrator admin = this.administratorService.findAll().iterator().next();
		final Collection<Actor> recipients = new ArrayList<Actor>();
		final Member member = enrolment.getMember();
		recipients.add(member);
		recipients.add(brotherhood);

		message.setMoment(new Date(System.currentTimeMillis() - 100));
		message.setSender(admin);
		message.setRecipients(recipients);
		message.setPriority("HIGH");
		message.setSpam(false);
		message.setTags("");
		message.setSubject("Enrolment accepted / Inscripcion aceptada");
		if (enrolment.getPosition() != null)
			message.setBody("Brotherhood " + brotherhood.getTitle() + " has accepted " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + " enrolment. Position given is " + enrolment.getPosition().getTitle() + ". / "
				+ "La hermandad  " + brotherhood.getTitle() + " ha aceptado la inscription de " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + ". La posicion otorgada es" + enrolment.getPosition().getTitle());
		else
			message.setBody("Member " + member.getName() + " " + member.getMiddleName() + " " + member.getSurname() + " dropped out from brotherhood " + brotherhood.getTitle() + ". / " + "El miembro " + member.getName() + " " + member.getMiddleName()
				+ " " + member.getSurname() + " ha abandonado la hermandad " + brotherhood.getTitle());
		this.messageService.saveAdmin(message);

		return result;
	}
	public void dropOut(final Enrolment enrolment) {
		enrolment.setPosition(null);
		enrolment.setDropOutDate(Calendar.getInstance().getTime());

		this.save(enrolment);
	}

	public Collection<Enrolment> getActiveEnrolments(final Member member) {
		final Collection<Enrolment> res = this.enrolmentRepository.activeEnrolments(member.getId());
		return res;
	}

	public Collection<Enrolment> getActiveBrotherhoodEnrolments(final Brotherhood brotherhood) {
		final Collection<Enrolment> res = this.enrolmentRepository.activeEnrolmentsBrotherhood(brotherhood.getId());
		return res;
	}

	public Collection<Enrolment> getPendingBrotherhoodEnrolments(final Brotherhood brotherhood) {
		final Collection<Enrolment> res = this.enrolmentRepository.pendingEnrolmentsBrotherhood(brotherhood.getId());
		return res;
	}

	public Collection<Enrolment> getDropOutBrotherhoodEnrolments(final Brotherhood brotherhood) {
		final Collection<Enrolment> res = this.enrolmentRepository.dropOutEnrolmentsBrotherhood(brotherhood.getId());
		return res;
	}

	public Collection<Enrolment> getDropOutMemberEnrolments(final Member member) {
		final Collection<Enrolment> res = this.enrolmentRepository.dropOutEnrolmentsMember(member.getId());
		return res;
	}

	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult bindingResult) {
		Enrolment result;

		if (enrolment.getId() == 0)
			result = enrolment;
		else {
			final Enrolment e = this.enrolmentRepository.findOne(enrolment.getId());
			result = new Enrolment(e);
			result.setId(e.getId());
			result.setVersion(e.getVersion());
			result.setMoment(enrolment.getMoment());
			result.setPosition(enrolment.getPosition());
			result.setMember(enrolment.getMember());
			result.setBrotherhood(enrolment.getBrotherhood());

		}
		return result;

	}

}
