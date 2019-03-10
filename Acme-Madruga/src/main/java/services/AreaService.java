
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
import domain.Area;

@Service
@Transactional
public class AreaService {

	//Managed Repository
	@Autowired
	private AreaRepository	areaRepository;

	@Autowired
	private ActorService	actorService;


	//Constructor
	public AreaService() {
		super();
	}

	// CRUD
	public Area create() {
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
		Area result;
		Assert.notNull(area);
		result = this.areaRepository.save(area);
		return result;
	}
	public void delete(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(area.getId() != 0);
		Assert.isTrue(this.areaRepository.exists(area.getId()));

		final Actor principal = this.actorService.findByPrincipal();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		Assert.isTrue(principal.getUserAccount().getAuthorities().contains(auth));

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
