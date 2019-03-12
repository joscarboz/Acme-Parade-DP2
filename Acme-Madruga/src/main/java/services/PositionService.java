
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import security.Authority;
import domain.Actor;
import domain.Member;
import domain.Position;

@Service
@Transactional
public class PositionService {

	//Managed Repository
	@Autowired
	private PositionRepository	positionRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MemberService		memberService;


	//Constructor
	public PositionService() {
		super();
	}

	// CRUD
	public Position create() {
		Position result;

		result = new Position();

		return result;

	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();

		return result;
	}

	public Position save(final Position position) {
		Position result;
		Assert.notNull(position);
		result = this.positionRepository.save(position);
		return result;
	}
	public void delete(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		Assert.isTrue(this.positionRepository.exists(position.getId()));

		final Collection<Member> members = this.memberService.findByPosition(position.getId());
		Assert.isTrue(members.isEmpty());

		final Actor principal = this.actorService.findByPrincipal();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		Assert.isTrue(principal.getUserAccount().getAuthorities().contains(auth));

		this.positionRepository.delete(position);

	}

	public Position findOne(final Integer positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);

		return result;

	}

	public void flush() {
		this.positionRepository.flush();
	}

}
