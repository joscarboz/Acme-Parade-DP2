
package services;

import java.util.Collection;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FloatRepository;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Service
@Transactional
public class FloatService {

	@Autowired
	private FloatRepository		floatRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	public FloatService() {
		super();
	}

	public Collection<Float> findAll() {
		Collection<Float> result;

		result = this.floatRepository.findAll();

		return result;
	}

	public Float findOne(final int floatId) {
		Assert.isTrue(floatId != 0);

		Float result;

		result = this.floatRepository.findOne(floatId);

		return result;
	}

	public Float create() {

		final Float result = new Float();

		result.setPictures(new LinkedList<String>());

		return result;
	}

	public Float save(final Float flot) {

		Assert.notNull(flot);

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final Float result = this.floatRepository.save(flot);

		if (flot.getId() == 0)
			brotherhood.getFloats().add(result);

		this.brotherhoodService.save(brotherhood);

		return result;
	}

	public void delete(final Float flot) {

		Assert.notNull(flot);

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final Collection<Float> floats = brotherhood.getFloats();

		Assert.isTrue(floats.contains(flot));

		floats.remove(flot);

		for (final Procession p : brotherhood.getProcessions())
			p.getFloats().remove(flot);

		this.brotherhoodService.save(brotherhood);

		this.floatRepository.delete(flot);
	}
}
