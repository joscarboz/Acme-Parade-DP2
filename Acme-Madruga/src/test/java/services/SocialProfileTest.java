
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.SocialProfile;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SocialProfileTest extends AbstractTest {

	//SUT
	@Autowired
	private SocialProfileService	socialProfileService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un SocialProfile
				"member1", "sampleNick", "http://twitter.com/users/demouser", "Twitter", null
			}, {//Anonimo no puede crear un SocialProfile
				null, "sampleNick", "http://twitter.com/users/demouser", "Twitter", IllegalArgumentException.class
			}, {// URL Incorrecta
				"member1", "sampleNick", "Esto no es un link", "Twitter", ConstraintViolationException.class
			}, { // Nick vacío
				"member1", "", "http://twitter.com/users/demouser", "Twitter", ConstraintViolationException.class
			}, { // Red Social vacio
				"member1", "sampleNick", "http://twitter.com/users/demouser", "", ConstraintViolationException.class
			}, { // URL Vacio
				"member1", "sampleNick", "", "Twitter", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Un member puede borrar uno de sus social profiles
				"member1", "socialProfile1", null
			}, { //Un usuario no puede borrar un social profile de otro usuario
				"member2", "socialProfile1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String nick, final String profileLink, final String socialNetwork, final Class<?> expected) {
		Class<?> caught;
		SocialProfile socialProfile;

		caught = null;
		try {
			this.authenticate(userName);
			socialProfile = this.socialProfileService.create();
			socialProfile.setNick(nick);
			socialProfile.setProfileLink(profileLink);
			socialProfile.setSocialNetwork(socialNetwork);
			this.socialProfileService.save(socialProfile);
			this.socialProfileService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String socialProfileBeanName, final Class<?> expected) {
		Class<?> caught;
		int socialProfileId;
		SocialProfile socialProfile;

		caught = null;
		try {
			this.authenticate(userName);
			socialProfileId = super.getEntityId(socialProfileBeanName);
			socialProfile = this.socialProfileService.findOne(socialProfileId);
			this.socialProfileService.delete(socialProfile);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
