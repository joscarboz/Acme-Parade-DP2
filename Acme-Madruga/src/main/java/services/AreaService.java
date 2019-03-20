package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import security.Authority;
import domain.Actor;
import domain.Administrator;
import domain.Area;
import domain.Brotherhood;

@Service
@Transactional
public class AreaService {

	// Managed Repository
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ActorService actorService;

	@Autowired
	private AdministratorService adminService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Constructor
	public AreaService() {
		super();
	}

	// CRUD
	public Area create() {

		Administrator logged = adminService.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		Area result;

		result = new Area();
		result.setName("Default");
		result.setPictures(new HashSet<String>());

		return result;

	}

	public Collection<Area> findAll() {
		Collection<Area> result;

		result = this.areaRepository.findAll();

		return result;
	}

	public Area save(final Area area) {
		Administrator logged = adminService.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		Area result;
		Assert.notNull(area);
		result = this.areaRepository.save(area);
		return result;
	}

	public void delete(final Area area) {

		Administrator logged = adminService.findByPrincipal();

		Assert.isTrue(logged instanceof Administrator);
		Assert.notNull(area);
		Assert.isTrue(area.getId() != 0);
		Assert.isTrue(this.areaRepository.exists(area.getId()));

		Collection<Brotherhood> brotherhoods = this.brotherhoodService
				.findByArea(area.getId());

		Assert.isTrue(brotherhoods.isEmpty());

		final Actor principal = this.actorService.findByPrincipal();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		Assert.isTrue(principal.getUserAccount().getAuthorities()
				.contains(auth));

		this.areaRepository.delete(area);

	}

	public Area findOne(final Integer areaId) {
		Area result;

		result = this.areaRepository.findOne(areaId);

		return result;

	}

	public Collection<Area> findByName(final String area) {
		Collection<Area> result;

		result = this.areaRepository.findByName(area);

		return result;

	}

	public void flush() {
		this.areaRepository.flush();
	}

}
