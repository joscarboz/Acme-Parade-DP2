
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import forms.RegisterMemberForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActorServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private MemberService	memberService;


	// Tests ------------------------------------------------------------------
	// Covers 5.6% of the data in the project
	//Covers 1620 sentences

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Normal creation of actor
				"password", "password", "usename", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", null
			}, { //Wrong pass
				"password", "null", "usename", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", IllegalArgumentException.class
			}, { //Incorrect email
				"password", "password", "username", "name", "middleName", "surname", "https://photo.com", "email", "address", "+34 344654843", ConstraintViolationException.class
			}, { //Incorrect name
				"password", "password", "username", "", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", ConstraintViolationException.class
			}, { //Incorrect surname
				"password", "password", "username", "name", "middleName", "", "https://photo.com", "email@acme.com", "address", "+34 344654843", ConstraintViolationException.class
			}, { //Incorrect picture
				"password", "password", "username", "name", "middleName", "surname", "photo", "email@acme.com", "address", "+34 344654843", ConstraintViolationException.class
			}, { //Incorrect username
				"password", "password", "", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String password, final String password2, final String userName, final String name, final String middleName, final String surname, final String photo, final String email, final String address,
		final String phone, final Class<?> expected) {
		Class<?> caught;
		final RegisterMemberForm registerMemberForm = new RegisterMemberForm();

		caught = null;
		try {
			registerMemberForm.setPassword(password);
			registerMemberForm.setPassword2(password2);
			registerMemberForm.setUsername(userName);
			registerMemberForm.setName(name);
			registerMemberForm.setMiddleName(middleName);
			registerMemberForm.setSurname(surname);
			registerMemberForm.setPhone(phone);
			registerMemberForm.setPhoto(photo);
			registerMemberForm.setAddress(address);
			registerMemberForm.setEmail(email);
			this.memberService.register(registerMemberForm);
			this.memberService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
