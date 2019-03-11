
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	//Managed Repository
	@Autowired
	private SocialProfileRepository	socialprofileRepository;

	//Supporting Services
	@Autowired
	private ActorService			actorService;


	//Constructor
	public SocialProfileService() {
		super();
	}

	public SocialProfile create() {
		SocialProfile socialProfile;

		socialProfile = new SocialProfile();

		return socialProfile;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);

		SocialProfile result, db;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		if (socialProfile.getId() != 0)
			Assert.isTrue(actor.getSocialProfiles().contains(socialProfile));

		db = this.findOne(socialProfile.getId());
		result = this.socialprofileRepository.save(socialProfile);
		actor.removeSocialProfile(db);
		actor.addSocialProfile(result);
		this.actorService.save(actor);

		return result;
	}

	public void delete(final SocialProfile socialProfile) {
		Actor actor;

		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		Assert.isTrue(this.socialprofileRepository.exists(socialProfile.getId()));

		actor = this.actorService.findByPrincipal();

		actor.removeSocialProfile(socialProfile);
		this.actorService.save(actor);
		this.socialprofileRepository.delete(socialProfile);
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialprofileRepository.findAll();

		return result;
	}

	public Collection<SocialProfile> findByPincipal() {
		Collection<SocialProfile> result;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		result = actor.getSocialProfiles();

		return result;
	}

	public SocialProfile findOne(final int socialIdentityId) {
		SocialProfile socialIdentity;

		socialIdentity = this.socialprofileRepository.findOne(socialIdentityId);
		return socialIdentity;
	}

	public void flush() {
		this.socialprofileRepository.flush();
	}
}
