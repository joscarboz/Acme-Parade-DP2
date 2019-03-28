
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Parade;
import domain.Segment;
import forms.SegmentForm;

@Service
@Transactional
public class SegmentService {

	@Autowired
	public SegmentRepository	segmentRepository;

	@Autowired
	public ParadeService		paradeService;

	@Autowired
	public BrotherhoodService	brotherhoodService;


	public SegmentService() {
		super();
	}

	public Segment findOne(final int id) {
		return this.segmentRepository.findOne(id);
	}

	public Collection<Segment> findAll() {
		return this.segmentRepository.findAll();
	}

	public Segment create(final int paradeId) {
		final Segment result = new Segment();
		final Segment lastSegment = this.findLastSegmentFromParade(paradeId);
		if (lastSegment != null)
			result.setOrigin(lastSegment.getDestination());

		return result;
	}

	public void deleteOne(final int id, final int paradeId) {
		final Segment segment = this.segmentRepository.findOne(id);
		this.segmentRepository.delete(id);
		final Parade parade = this.paradeService.findOne(paradeId);
		parade.getSegments().remove(segment);
		this.paradeService.save(parade);
	}

	public void deleteInBatch(final Collection<Segment> segments) {
		this.segmentRepository.deleteInBatch(segments);
	}

	public void deleteAllFromParade(final int paradeId) {
		final Parade parade = this.paradeService.findOne(paradeId);

		Assert.isTrue(this.brotherhoodService.findByPrincipal().getParades().contains(parade));

		parade.getSegments().clear();
		this.paradeService.save(parade);
		this.deleteInBatch(parade.getSegments());

	}

	public Segment findLastSegmentFromParade(final int paradeId) {
		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.isTrue(this.brotherhoodService.findByPrincipal().getParades().contains(parade));
		final List<Segment> paradeSegments = new ArrayList<Segment>(parade.getSegments());
		if (paradeSegments.isEmpty())
			return null;
		return paradeSegments.get(paradeSegments.size() - 1);
	}

	public void deleteLastSegmentFromParade(final int paradeId) {
		this.deleteOne(this.findLastSegmentFromParade(paradeId).getId(), paradeId);
	}

	public Segment save(final Segment segment, final Parade parade) {
		final Segment result;
		Assert.isTrue(segment.getOriginReachMoment().before(segment.getDestinationReachMoment()));
		final Segment lastSegment = this.findLastSegmentFromParade(parade.getId());
		final List<Segment> segments = new ArrayList<Segment>(parade.getSegments());
		final boolean isFirst = segments.get(0).equals(segment);
		final boolean isLast = segment.equals(lastSegment);

		if (segment.getId() == 0) {
			if (lastSegment != null) {
				Assert.isTrue(lastSegment.getDestination().equals(segment.getOrigin()));
				Assert.isTrue(segment.getOriginReachMoment().after(lastSegment.getDestinationReachMoment()));
			}
			result = this.segmentRepository.save(segment);
			parade.getSegments().add(result);
			this.paradeService.save(parade);
		} else {
			if (isFirst && !isLast) {
				final Segment nextSegment = segments.get(segments.indexOf(segment) + 1);
				Assert.isTrue(segment.getDestination().equals(nextSegment.getOrigin()));
				Assert.isTrue(segment.getDestinationReachMoment().before(nextSegment.getOriginReachMoment()));
			}
			if (isLast && !isFirst) {
				final Segment previousSegment = segments.get(segments.indexOf(segment) - 1);
				Assert.isTrue(segment.getOrigin().equals(previousSegment.getDestination()));
				Assert.isTrue(segment.getOriginReachMoment().after(previousSegment.getDestinationReachMoment()));
			}
			if (!isLast && !isFirst) {
				final Segment nextSegment = segments.get(segments.indexOf(segment) + 1);
				final Segment previousSegment = segments.get(segments.indexOf(segment) - 1);
				Assert.isTrue(segment.getDestination().equals(nextSegment.getOrigin()));
				Assert.isTrue(segment.getOrigin().equals(previousSegment.getDestination()));
				Assert.isTrue(segment.getDestinationReachMoment().before(nextSegment.getOriginReachMoment()));
				Assert.isTrue(segment.getOriginReachMoment().after(previousSegment.getDestinationReachMoment()));
			}
			result = this.segmentRepository.save(segment);
		}

		return result;
	}
	public Segment reconstruct(final SegmentForm segmentForm) {
		Segment result;

		result = new Segment();

		result.setId(segmentForm.getId());
		result.setVersion(segmentForm.getVersion());
		result.setOrigin(segmentForm.getOrigin());
		result.setDestination(segmentForm.getDestination());
		result.setOriginReachMoment(segmentForm.getOriginReachMoment());
		result.setDestinationReachMoment(segmentForm.getDestinationReachMoment());

		return result;

	}

	public SegmentForm toSegmentForm(final Segment segment, final Parade parade) {
		SegmentForm result;

		result = new SegmentForm();
		result.setOrigin(segment.getOrigin());
		result.setVersion(segment.getVersion());
		result.setDestination(segment.getDestination());
		result.setOriginReachMoment(segment.getOriginReachMoment());
		result.setDestinationReachMoment(segment.getDestinationReachMoment());
		result.setId(segment.getId());
		result.setParadeId(parade.getId());

		return result;

	}

	public void flush() {
		this.segmentRepository.flush();

	}
}
