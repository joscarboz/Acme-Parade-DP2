
package services;

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import forms.RegisterBrotherhoodForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private BrotherhoodService	brotherhoodService;

	Date						fechaMinima	= new Date();


	// Tests ------------------------------------------------------------------
	// Covers 6.7% of the data in the project
	//Covers 1929 sentences

	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Normal actor creation
				"password", "password", "usename", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, null
			}, { //Wrong pass
				"password", "null", "usename", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, IllegalArgumentException.class
			}, { //Wrong email
				"password", "password", "username", "name", "middleName", "surname", "https://photo.com", "email", "address", "+34 344654843", "Title", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong name
				"password", "password", "username", "", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong surname
				"password", "password", "username", "name", "middleName", "", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong photo
				"password", "password", "username", "name", "middleName", "surname", "photo", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong username
				"password", "password", "", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong title
				"password", "password", "username", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "", this.fechaMinima, ConstraintViolationException.class
			}, { //Wrong date
				"password", "password", "username", "name", "middleName", "surname", "https://photo.com", "email@acme.com", "address", "+34 344654843", "Title", null, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Date) testingData[i][11], (Class<?>) testingData[i][12]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String password, final String password2, final String userName, final String name, final String middleName, final String surname, final String photo, final String email, final String address,
		final String phone, final String title, final Date date, final Class<?> expected) {
		Class<?> caught;
		final RegisterBrotherhoodForm registerBrotherhoodForm = new RegisterBrotherhoodForm();

		caught = null;
		try {
			registerBrotherhoodForm.setPassword(password);
			registerBrotherhoodForm.setPassword2(password2);
			registerBrotherhoodForm.setUsername(userName);
			registerBrotherhoodForm.setName(name);
			registerBrotherhoodForm.setMiddleName(middleName);
			registerBrotherhoodForm.setSurname(surname);
			registerBrotherhoodForm.setPhone(phone);
			registerBrotherhoodForm.setPhoto(photo);
			registerBrotherhoodForm.setAddress(address);
			registerBrotherhoodForm.setEmail(email);
			registerBrotherhoodForm.setTitle(title);
			registerBrotherhoodForm.setEstablishment(date);
			this.brotherhoodService.register(registerBrotherhoodForm);
			this.brotherhoodService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
